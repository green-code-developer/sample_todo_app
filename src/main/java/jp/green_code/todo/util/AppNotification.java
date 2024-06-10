package jp.green_code.todo.util;

public class AppNotification {
    private AppMessage appMessage;

    public static final String NOTIFICATION = "notification";

    private NotificationType type;

    public static AppNotification successNotification(AppMessage appMessage) {
        var result = new AppNotification();
        result.type = NotificationType.SUCCESS;
        result.appMessage = appMessage;
        return result;
    }

    private AppNotification() {}

    public static AppNotification errorNotification(AppMessage appMessage) {
        var result = new AppNotification();
        result.type = NotificationType.ERROR;
        result.appMessage = appMessage;
        return result;
    }

    public AppMessage getAppMessage() {
        return appMessage;
    }

    public NotificationType getType() {
        return type;
    }

    public enum NotificationType {
        SUCCESS, ERROR
    }
}
