package jp.green_code.todo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class JsonUtil {
    public static ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    public static String toJson(Object o, boolean isSilent) {
        try {
            return objectMapper().writeValueAsString(o);
        } catch (Exception e) {
            if (isSilent) {
                return null;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public static String toJson(Object o) {
        return toJson(o, false);
    }

    public static String toJsonSilent(Object o) {
        return toJson(o, true);
    }

    public static <T> T fromString(String s, Class<T> clazz) {
        return fromString(s, clazz, false);
    }

    public static <T> T fromString(String s, Class<T> clazz, boolean quiet) {
        try {
            return objectMapper().readValue(s, clazz);
        } catch (Exception e) {
            if (quiet) {
                return null;
            }
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromString(String s, TypeReference<T> type) {
        return fromString(s, type, false);
    }

    public static <T> T fromString(String s, TypeReference<T> type, boolean quiet) {
        try {
            return objectMapper().readValue(s, type);
        } catch (Exception e) {
            if (quiet) {
                return null;
            }
            throw new RuntimeException(e);
        }
    }
}
