package com.example.todo.enums;

import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public enum TodoStatusEnum {
    NEW("未処理"),
    DOING("処理中"),
    DONE("完了"),
    DELETED("削除済"),
    ;

    private String label;

    private TodoStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // ステータス（String）をラベルに変換
    // 不正な値の場合は未処理を返却
    public static String toLabel(String status) {
        Optional<TodoStatusEnum> value = Arrays.stream(TodoStatusEnum.values()).filter(e -> StringUtils.equals(e + "", status)).findFirst();
        return value.isPresent() ? value.get().label : NEW.label;
    }
}
