package jp.green_code.todo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jooq.SortField;

import java.util.List;

import static jp.green_code.todo.jooq.Tables.TODO_;
import static lombok.AccessLevel.PRIVATE;

/**
 * やること検索の並び順
 */
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum TodoSearchSortEnum {
    UPDATE_DESC(List.of(TODO_.UPDATED_AT.desc())),
    UPDATE_ASC(List.of(TODO_.UPDATED_AT.asc())),
    STATUS(List.of(TODO_.TODO_STATUS.desc())),
    DEADLINE_DESC(List.of(TODO_.DEADLINE.desc())),
    DEADLINE_ASC(List.of(TODO_.TODO_STATUS.asc())),
    ;

    private final List<SortField<?>> sort;
}
