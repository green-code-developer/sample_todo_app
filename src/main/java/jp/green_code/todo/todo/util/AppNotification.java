package jp.green_code.todo.todo.util;

public class AppNotification {
    enum NotificationType {
        SUCCESS, ERROR
    }

    public static final String NOTIFICATION = "notification";

    private NotificationType type;
    private String messageKey;
    private Object[] params = new Object[]{};

    private AppNotification() {}

    public static AppNotification successNotification(String messageKey) {
        return successNotification(messageKey, new Object[]{});
    }

    public static AppNotification successNotification(String messageKey, Object[] params) {
        var result = new AppNotification();
        result.type = NotificationType.SUCCESS;
        result.messageKey = messageKey;
        result.params = params;
        return result;
    }


    public static AppNotification errorNotification(String messageKey) {
        return errorNotification(messageKey, new String[]{});
    }

    public static AppNotification errorNotification(String messageKey, String[] params) {
        var result = new AppNotification();
        result.type = NotificationType.ERROR;
        result.messageKey = messageKey;
        result.params = params;
        return result;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getParams() {
        return params;
    }
}
