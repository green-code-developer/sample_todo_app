package jp.green_code.shared.dto;

public record Notification(NotificationType type, MessageKey messageKey) {
    public static final String NOTIFICATION = "notification";

    public enum NotificationType {
        NOTIFICATION_SUCCESS, NOTIFICATION_ERROR
    }
}
