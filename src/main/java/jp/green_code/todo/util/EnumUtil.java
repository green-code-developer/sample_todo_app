package jp.green_code.todo.util;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import jp.green_code.todo.enums.TodoSearchSortEnum;
import jp.green_code.todo.enums.TodoStatusEnum;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

// Thymeleaf からEnum を呼び出す
// 画面で使用するため値をラベル（表示用文字列）に変換する
// intelliJ では未使用に見えるかもしれないが、Thymeleaf から呼んでいるので注意
@Component("enumUtil")
public class EnumUtil {

    private final MessageUtil messageUtil;


    public static <T extends Enum<T>> Optional<T> valueOf(Class<T> clazz, String name) {
        // https://stackoverflow.com/a/32176739
        return EnumSet.allOf(clazz).stream().filter(v -> v.name().equals(name))
            .findAny();
    }

    public EnumUtil(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    List<Pair<String, String>> enumValues(Object[] values, Class clazz) {
        return Arrays.stream(values).map(e -> Pair.of(e + "", enumToMessage(e, clazz))).toList();
    }

    String enumToMessage(Object e, Class clazz) {
        return messageUtil.getMessage("enum." + clazz.getSimpleName() + "." + e);
    }

    public List<Pair<String, String>> getTodoSearchSortEnum() {
        return enumValues(TodoSearchSortEnum.values(), TodoSearchSortEnum.class);
    }

    public List<Pair<String, String>> getTodoStatusEnum() {
        return enumValues(TodoStatusEnum.values(), TodoStatusEnum.class);
    }

    // TodoStatus の表示用ラベルを取得
    // 不正値の場合は新規扱いとする
    public String labelOfTodoStatus(String name) {
        var e = EnumUtil.valueOf(TodoStatusEnum.class, name).orElse(TodoStatusEnum.NEW);
        return enumToMessage(e, TodoStatusEnum.class);
    }
}
