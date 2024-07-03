package jp.green_code.todo.enums;

import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

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
        return Arrays.stream(TodoStatusEnum.values())
            .filter(e -> StringUtils.equals(e + "", s)).findFirst();
    }
}
