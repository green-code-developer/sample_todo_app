package jp.green_code.todo.dto.common;

import lombok.Getter;

@Getter
public class AppMessage {

    private String code;
    private Object[] params = new Object[]{};
    private String message;

    public static AppMessage propertyMessage(String code) {
        return propertyMessage(code, new Object[]{});
    }

    public static AppMessage propertyMessage(String code, Object[] params) {
        var result = new AppMessage();
        result.code = code;
        result.params = params;
        return result;
    }

    public static AppMessage rawMessage(String message) {
        var result = new AppMessage();
        result.message = message;
        return result;
    }
}
