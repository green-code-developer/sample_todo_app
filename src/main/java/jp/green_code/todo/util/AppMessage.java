package jp.green_code.todo.util;

public class AppMessage {
    private final String code;
    private final Object[] params;

    public AppMessage(String code) {
        this.code = code;
        params = new Object[]{};
    }

    public AppMessage(String code, Object... params) {
        this.code = code;
        this.params = params;
    }

    public static AppMessage toMessage(String code) {
        return toMessage(code, new Object[]{});
    }

    public static AppMessage toMessage(String code, Object[] params) {
        return new AppMessage(code, params);
    }

    public String getCode() {
        return code;
    }

    public Object[] getParams() {
        return params;
    }
}
