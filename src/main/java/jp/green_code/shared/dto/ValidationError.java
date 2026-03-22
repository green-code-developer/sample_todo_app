package jp.green_code.shared.dto;

public record ValidationError(
        String code, MessageKey messageKey
) {
    public ValidationError(MessageKey messageKey) {
        this(messageKey.key(), messageKey);
    }
}
