package jp.green_code.todo.enums;

import lombok.Getter;
import org.springframework.data.domain.Sort;

/**
 * やること検索の並び順
 */
@Getter
public enum TodoSearchSortEnum {
    UPDATE_DESC(Sort.by("updated_at").descending()),
    UPDATE_ASC(Sort.by("updated_at").ascending()),
    STATUS(Sort.by("todo_status").descending()),
    DEADLINE_DESC(Sort.by("deadline").descending()),
    DEADLINE_ASC(Sort.by("deadline").ascending()),
    ;

    private Sort sort;

    TodoSearchSortEnum(Sort sort) {
        this.sort = sort;
    }
}
