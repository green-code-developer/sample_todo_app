package jp.green_code.todo.util;

import static org.springframework.web.servlet.support.RequestContextUtils.getInputFlashMap;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.web.servlet.support.RequestContextUtils;

public class ControllerUtil {
    public static <T> T getFlashAttribute(HttpServletRequest request, String key) {
        Map<String, ?> inputFlashMap = getInputFlashMap(request);
        if (inputFlashMap != null) {
            //noinspection unchecked
            return (T) inputFlashMap.get(key);
        }
        return null;
    }
}
