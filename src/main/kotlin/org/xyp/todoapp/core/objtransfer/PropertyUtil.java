package org.xyp.todoapp.core.objtransfer;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    private PropertyUtil() {
    }

    public static final String PROPERTY_SPLITTER = ".";
    public static final String ARR_INDEX_PATTERN = "^(.*)\\[(\\d+)]$";

    public static Object getProperty(Object object, String property) {
        if (null == object) {
            return null;
        }
        if (!StringUtils.hasText(property)) {
            return object;
        }
        final int nextSplitter = property.indexOf(PROPERTY_SPLITTER);
        final var firstProperty = -1 == nextSplitter ? property : property.substring(0, nextSplitter);
        final var arrayMatcher = Pattern.compile(ARR_INDEX_PATTERN).matcher(firstProperty);
        final boolean isArrayIdx = arrayMatcher.find();

        if (isArrayIdx) {
            final var arrProperty = arrayMatcher.group(1);
            final var idx = Integer.parseInt(arrayMatcher.group(2));
            final var item = getPropertyValueForList(object, arrProperty, idx);
            if (nextSplitter < 0) {
                return item;
            }
            return getProperty(item, property.substring(nextSplitter + 1));
        } else {
            if (object instanceof Iterable<?> iter) {
                return flatListObjects(property, iter.iterator(), firstProperty, nextSplitter);
            } else {
                return getPropertyValue(object, property, firstProperty, nextSplitter);
            }
        }
    }

    private static List<Object> flatListObjects(String property, Iterator<?> iter, String firstProperty, int nextSplitter) {
        final List<Object> result = new ArrayList<>();
        while (iter.hasNext()) {
            final var nextObj = iter.next();
            final var nextValue = getPropertyValue(nextObj, property, firstProperty, nextSplitter);
            if (nextValue instanceof Iterable<?> anotherIterable) {
                for (final var o : anotherIterable) {
                    result.add(o);
                }
            } else {
                result.add(nextValue);
            }
        }
        return result;
    }

    private static Object getPropertyValue(Object object, String property, String firstProperty, int nextSplitter) {
        if (object instanceof Map<?, ?> map) {
            final var nextObject = map.get(firstProperty);
            if (null == nextObject) {
                return null;
            }
            return nextSplitter < 0 ? nextObject : getProperty(nextObject, property.substring(nextSplitter + 1));
        } else {
            return processNonMap(object, property, firstProperty, nextSplitter);
        }
    }

    private static Object processNonMap(Object object, String property, String firstProperty, int nextSplitter) {
        try {
            final var nextObject = new BeanWrapperImpl(object).getPropertyValue(firstProperty);
            if (null == nextObject) {
                return null;
            }
            return nextSplitter < 0 ? nextObject : getProperty(nextObject, property.substring(nextSplitter + 1));
        } catch (Exception e) {
            return processError(e);
        }
    }

    private static String processError(Exception e) {
        logger.warn("return null value for error:");
        logger.warn(e.getMessage());
        return null;
    }

    private static Object getPropertyValueForList(Object object, String firstProperty, int idx) {
        final Object[] itemsHolder = new Object[1];
        if (object instanceof Map<?, ?> map) {
            itemsHolder[0] = map.get(firstProperty);
        } else {
            itemsHolder[0] = getProperty(object, firstProperty);
        }

        final Object items = itemsHolder[0];
        switch (items) {
            case List<?> list -> {
                if (list.size() <= idx) {
                    throw new IllegalArgumentException("Index out of bounds: " + idx + ", max size " + list.size());
                }
                return list.get(idx);
            }
            case Iterator<?> iter -> {
                return getItemFromIterator(iter, idx);
            }
            case Collection<?> collection -> {
                return getItemFromIterator(collection.iterator(), idx);
            }
            case null -> {
                return null;
            }
            default -> throw new IllegalArgumentException("expect iterable, but found " +
                Optional.ofNullable(items).map(Object::getClass).map(Class::getName).orElse(null));
        }
    }

    private static Object getItemFromIterator(Iterator<?> iterator, int idx) {
        int i = 0;
        while (iterator.hasNext()) {
            final var ret = iterator.next();
            if (i == idx) {
                return ret;
            }
            i++;
        }
        throw new IllegalArgumentException("Index out of bounds: " + idx + ", max boundary " + i);
    }
}
