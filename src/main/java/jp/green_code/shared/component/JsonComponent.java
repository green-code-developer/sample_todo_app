package jp.green_code.shared.component;

import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@Component("jsonComponent")
public class JsonComponent {
    public final ObjectMapper objectMapper;

    public JsonComponent(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Optional<String> toJson(Object o, boolean isSilent) {
        try {
            return Optional.of(objectMapper.writeValueAsString(o));
        } catch (Exception e) {
            if (isSilent) {
                return Optional.empty();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public String toJson(Object o) {
        return toJson(o, false).orElseThrow();
    }

    public Optional<String> toJsonSilent(Object o) {
        return toJson(o, true);
    }

    public <T> Optional<T> fromString(String s, Class<T> clazz) {
        return fromString(s, clazz, false);
    }

    public <T> Optional<T> fromString(String s, Class<T> clazz, boolean isSilent) {
        try {
            return Optional.of(objectMapper.readValue(s, clazz));
        } catch (JacksonException e) {
            if (isSilent) {
                return Optional.empty();
            }
            throw new RuntimeException(e);
        }
    }

    public <T> Optional<T> fromString(String s, TypeReference<T> type) {
        return fromString(s, type, false);
    }

    public <T> Optional<T> fromString(String s, TypeReference<T> type, boolean quiet) {
        try {
            return Optional.of(objectMapper.readValue(s, type));
        } catch (JacksonException e) {
            if (quiet) {
                return Optional.empty();
            }
            throw e;
        }
    }
}
