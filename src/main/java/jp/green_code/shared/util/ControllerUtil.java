package jp.green_code.shared.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Optional;

import static org.springframework.web.servlet.support.RequestContextUtils.getInputFlashMap;

public class ControllerUtil {
    public static <T> Optional<T> getFlashAttribute(HttpServletRequest request, String key, Class<T> clazz) {
        Map<String, ?> inputFlashMap = getInputFlashMap(request);
        if (inputFlashMap == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(inputFlashMap.get(key)).map(clazz::cast);
    }
}
