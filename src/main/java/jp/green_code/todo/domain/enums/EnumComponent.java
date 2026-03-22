package jp.green_code.todo.domain.enums;

import jp.green_code.shared.component.EnumLabelResolver;
import jp.green_code.shared.dto.EnumLabel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("enumComponent")
public class EnumComponent {

    private final EnumLabelResolver enumLabelResolver;

    public EnumComponent(EnumLabelResolver enumLabelResolver) {
        this.enumLabelResolver = enumLabelResolver;
    }

    public List<EnumLabel<TodoSearchSortEnum>> getTodoSearchSortEnum() {
        return enumLabelResolver.enumValues(TodoSearchSortEnum.class);
    }

    public List<EnumLabel<TodoStatusEnum>> getTodoStatusEnum() {
        return enumLabelResolver.enumValues(TodoStatusEnum.class);
    }
}
