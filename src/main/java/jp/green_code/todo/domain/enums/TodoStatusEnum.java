package jp.green_code.todo.domain.enums;

import java.util.Optional;

/**
 * やることステータス
 */
public enum TodoStatusEnum {
    NEW,
    DOING,
    DONE,
    DELETED,
    ;

    public static Optional<TodoStatusEnum> optionalValueOf(String s) {
        try {
            return Optional.of(TodoStatusEnum.valueOf(s));
        } catch (IllegalArgumentException | NullPointerException e) {
            return Optional.empty();
        }
    }
}
