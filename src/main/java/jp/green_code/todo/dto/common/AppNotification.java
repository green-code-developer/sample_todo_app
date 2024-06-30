package jp.green_code.todo.dto.common;

import static jp.green_code.todo.dto.common.AppNotification.NotificationType.ERROR;
import static jp.green_code.todo.dto.common.AppNotification.NotificationType.SUCCESS;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppNotification {
    private AppMessage appMessage;

    public static final String NOTIFICATION = "notification";

    private NotificationType type;

    public static AppNotification successNotification(AppMessage appMessage) {
        var result = new AppNotification();
        result.type = SUCCESS;
        result.appMessage = appMessage;
        return result;
    }

    public static AppNotification errorNotification(AppMessage appMessage) {
        var result = new AppNotification();
        result.type = ERROR;
        result.appMessage = appMessage;
        return result;
    }

    public enum NotificationType {
        SUCCESS, ERROR
    }
}
