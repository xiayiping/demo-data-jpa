package org.xyp.todoapp.core.json;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

public class JsonHelper {
    private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);

    private JsonHelper() {
    }

    public static String objToJsonString(@NonNull Object o) {
        return getJsonMapper().writeValueAsString(o);
    }

    public static <T> T jsonToObject(@NonNull String json, @NonNull Class<T> targetType) {
        return getJsonMapper().readValue(json, targetType);
    }

    public static @NonNull Map<String, Object> jsonToMap(@NonNull String json) {
        return getJsonMapper().readValue(json, new TypeReference<>() {
        });
    }

    public static <T> T jsonToObject(@NonNull String json, @NonNull TypeReference<T> targetType) {
        return getJsonMapper().readValue(json, targetType);
    }

    enum OBJS {
        json_mapper;

        private final JsonMapper jsonMapper;

        OBJS() {
            final var jsonMapperBuilder = JsonMapper.builder();
            jsonMapperBuilder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            jsonMapper = jsonMapperBuilder.build();
        }

        JsonMapper get() {
            return jsonMapper;
        }

    }

    public static JsonMapper getJsonMapper() {
        return OBJS.json_mapper.jsonMapper;
    }
}
