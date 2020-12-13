package com.sihenzhang.chat.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static String toJsonString(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException ignored) {
            // do nothing
        }
        return null;
    }

    public static <T> T parseObject(String json, Class<T> className) {
        try {
            return OBJECT_MAPPER.readValue(json, className);
        } catch (JsonProcessingException ignored) {
            // do nothing
        }
        return null;
    }
}
