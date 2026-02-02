package jp.green_code.todo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static jp.green_code.todo.repository.base.BaseTodoRepository.Columns.DEADLINE;
import static jp.green_code.todo.repository.base.BaseTodoRepository.Columns.TODO_ID;
import static jp.green_code.todo.repository.base.BaseTodoRepository.Columns.TODO_STATUS;
import static jp.green_code.todo.repository.base.BaseTodoRepository.Columns.UPDATED_AT;
import static lombok.AccessLevel.PRIVATE;

/**
 * やること検索の並び順
 */
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum TodoSearchSortEnum {
    UPDATE_DESC("%s desc, %s desc".formatted(UPDATED_AT, TODO_ID)),
    UPDATE_ASC("%s asc, %s asc".formatted(UPDATED_AT, TODO_ID)),
    STATUS("%s desc, %s desc".formatted(TODO_STATUS, TODO_ID)),
    DEADLINE_DESC("%s desc, %s desc".formatted(DEADLINE, TODO_ID)),
    DEADLINE_ASC("%s asc, %s asc".formatted(DEADLINE, TODO_ID)),
    ;

    private final String sort;
}
