package org.xyp.todoapp.core.objtransfer;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.xyp.todoapp.core.json.JsonHelper;

import java.util.*;

public class MapTransfer {
    private static final Logger logger = LoggerFactory.getLogger(MapTransfer.class);

    private static final String EXT_ROOT_KEY = "$$_ext_root";
    private static final String EXT_ARR_KEY = "$$_ext_array";
    private static final String EXT_RANGE_KEY = "$$_ext_array_range";
    private static final String EXT_ARR_FILTER = "$$_ext_array_filter";
    private static final Set<String> IGNORE = Set.of(
        EXT_ROOT_KEY,
        EXT_ARR_KEY,
        EXT_RANGE_KEY,
        EXT_ARR_FILTER
    );

    private MapTransfer() {
    }

    public static Object transferJson(@NonNull Object context, @NonNull String transferConfigJson) {
        final var configMap = JsonHelper.jsonToMap(transferConfigJson);
        return transferMap(context, configMap);
    }

    private static Object transferMap(@NonNull Object context, @NonNull Map<String, ?> configMap) {
        if (configMap.containsKey(EXT_ARR_KEY)) {
            final var collection = extractProperty(context, (String) configMap.get(EXT_ARR_KEY));
            return expandMapToList(context, collection, configMap);
        } else {
            final var rootContext = findRootContext(context, configMap);
            return transferMapWithContext(rootContext, configMap);
        }
    }

    private static Object expandMapToList(@NonNull Object context, Object collection, Map<String, ?> configMap) {
        if (collection instanceof String cStr && !StringUtils.hasText(cStr)) {
            return List.of();
        }

        final Iterator<?> iterator = createIterator(collection);
        final var range = Optional.ofNullable(configMap.get(EXT_RANGE_KEY))
            .map(r -> {
                final var split = r.toString().split(",");
                return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
            })
            .orElse(new int[]{0, Integer.MAX_VALUE});

        int i = 0;
        List<Map.Entry<Integer, Object>> list = new ArrayList<>();
        while (iterator.hasNext() && i < range[1]) {
            final var next = iterator.next();
            if (i >= range[0]) {
                list.add(new AbstractMap.SimpleEntry<>(i, next));
            }
            i++;
        }

        final var filter = configMap.get(EXT_ARR_FILTER);
        return list.stream()
            .map(ent -> Map.of(
                "parent", context,
                "item", ent.getValue(),
                "idx", ent.getKey(),
                "iNum", ent.getKey() + 1
            ))
            .filter(map -> filterListItem(map, filter))
            .map(map -> transferMapWithContext(map, configMap))
            .toList();

    }

    private static boolean filterListItem(Map<String, Object> map, Object filter) {
        if (null == filter) {
            logger.debug("there's no filter provided, return pass.");
            return true;
        }

        final var checked = PropertyTransfer.parseExpression(map, filter.toString());
        if (checked instanceof Boolean b) {
            return b;
        }
        try {
            return Boolean.parseBoolean(checked.toString());
        } catch (Exception e) {
            logger.warn("exception while checking [{}]", filter);
            logger.warn("return passed for [{}]", e.getMessage());
            return true;
        }
    }

    private static Map<String, ?> transferMapWithContext(Object rootContext, @NonNull Map<String, ?> configMap) {
        final var keys = configMap.entrySet().stream()
            .filter(ent -> !IGNORE.contains(ent.getKey()))
            .toList();
        if (keys.isEmpty() && rootContext instanceof Map<?, ?> rootMap) {
            return (Map<String, ?>) rootMap;
        }
        return keys.stream()
            .map(ent -> extractToTuple(rootContext, ent.getKey(), ent.getValue()))
            .filter(Objects::nonNull)
            .collect(
                HashMap::new,
                (map, tuple) -> map.put(tuple.getKey(), tuple.getValue()),
                HashMap::putAll
            );
    }

    private static Map.Entry<String, ?> extractToTuple(@NonNull Object rootContext, @NonNull String key, Object value) {
        if (value instanceof String stringValue) {
            return new AbstractMap.SimpleEntry<>(key, PropertyTransfer.parseExpression(rootContext, stringValue));
        } else if (value instanceof Map<?, ?> innerMap) {
            return new AbstractMap.SimpleEntry<>(
                key,
                transferMap(rootContext, (Map<String, ?>) innerMap)
            );
        } else if (value instanceof List<?> innerList) {
            return new AbstractMap.SimpleEntry<>(
                key,
                transferList(rootContext, innerList)
            );
        }
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    private static Object transferList(@NonNull Object rootContext, @NonNull List<?> innerList) {

        return innerList.stream()
            .map(item -> {
                if (item instanceof Map<?, ?> map) {
                    return transferMap(rootContext, (Map<String, ?>) map);
                } else if (item instanceof List<?> list) {
                    return transferList(rootContext, list);
                } else if (item instanceof String string) {
                    return PropertyTransfer.parseExpression(rootContext, string);
                }
                return item;
            }).toList();
    }

    private static Iterator<?> createIterator(@NonNull Object rootContext) {
        if (rootContext instanceof Iterable<?> iterable) {
            return iterable.iterator();
        }
        throw new IllegalArgumentException("" + rootContext.getClass() + " is not an iterable");
    }

    private static Object findRootContext(@NonNull Object context, @NonNull Map<String, ?> configMap) {
        final var rootExp = configMap.get(EXT_ROOT_KEY);
        if (null != rootExp) {
            if (rootExp instanceof String expString) {
                return extractProperty(context, expString);
            } else {
                return rootExp;
            }
        } else {
            return context;
        }
    }

    private static Object extractProperty(@NonNull Object context, @NonNull String propertyWithBrace) {
        final var property = propertyWithBrace.trim()
            .replaceAll("}$", "")
            .replaceAll("^\\$\\{", "");

        return PropertyUtil.getProperty(context, property);
    }
}
