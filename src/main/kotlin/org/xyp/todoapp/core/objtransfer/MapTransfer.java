package org.xyp.todoapp.core.objtransfer;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        } else {
            final var rootContext = findRootContext(context, configMap);
            return transferMapWithContext(rootContext, configMap);
        }
        return null;
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
        return null;
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
