package jp.green_code.todo.repository;

import jp.green_code.todo.dto.common.AppPageableDto;
import jp.green_code.todo.dto.common.AppPageableList;
import jp.green_code.todo.enums.TodoSearchSortEnum;
import jp.green_code.todo.enums.TodoStatusEnum;
import jp.green_code.todo.jooq.tables.pojos.Todo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static jp.green_code.todo.enums.TodoSearchSortEnum.UPDATE_DESC;
import static jp.green_code.todo.jooq.Tables.TODO_;
import static org.jooq.impl.DSL.trueCondition;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TodoRepository {

    private final DSLContext dsl;

    public Todo save(Todo account) {
        //@formatter:off
        return dsl.insertInto(TODO_).set(dsl.newRecord(TODO_, account))
                .onConflict(TODO_.TODO_ID).doUpdate()
                    .set(dsl.newRecord(TODO_, account))
                .returning(TODO_.TODO_ID).fetchOneInto(Todo.class);
        //@formatter:on
    }

    public Optional<Todo> findById(long id) {
        var res = dsl.select().from(TODO_).where(TODO_.TODO_ID.eq(id)).fetchOneInto(Todo.class);
        return ofNullable(res);
    }

    public AppPageableList<Todo> findByCondition(AppPageableDto pageable, TodoSearchSortEnum sort, String word, TodoStatusEnum status, OffsetDateTime deadlineFrom, OffsetDateTime deadlineTo) {
        //@formatter:off
        var condition = Stream.of(
                ofNullable(word).filter(StringUtils::isNotBlank).map(TODO_.DETAIL::contains),
                ofNullable(status).map(TODO_.TODO_STATUS::eq),
                ofNullable(deadlineFrom).map(from ->
                        TODO_.DEADLINE.isNull().or(TODO_.DEADLINE.greaterOrEqual(from))),
                ofNullable(deadlineTo).map(TODO_.DEADLINE::le)
        ).flatMap(Optional::stream).reduce(trueCondition(), Condition::and);

        // 参考 別の書き方 部分的にSQL を使うことで学習コストを下げる
        /*
        condition = Stream.of(
                ofNullable(word).filter(StringUtils::isNotBlank).map(w ->
                        DSL.condition("detail LIKE ?", "%" + w + "%")),
                ofNullable(status).map(TODO_.TODO_STATUS::eq),
                ofNullable(deadlineFrom).map(from ->
                        DSL.condition("deadline is null or cast(? as timestamp with time zone) <= deadline", from)),
                ofNullable(deadlineTo).map(TODO_.DEADLINE::le)
        ).flatMap(Optional::stream).reduce(trueCondition(), Condition::and);
        */

        long count = ofNullable(
                dsl.selectCount().from(TODO_).where(condition).fetchOne(0, long.class)
        ).orElse(0L);

        List<Todo> list = count == 0 ? List.of() : dsl.selectFrom(TODO_).where(condition)
                .orderBy(ofNullable(sort).orElse(UPDATE_DESC).getSort())
                .limit(pageable.getLimit())
                .offset(pageable.getOffset())
                .fetchInto(Todo.class);
        //@formatter:on

        return new AppPageableList<>(pageable, list, count);
    }
}
