package jp.green_code.todo.domain.repository;

import jp.green_code.shared.dto.PageRequest;
import jp.green_code.shared.dto.PageResult;
import jp.green_code.todo.domain.entity.TodoEntity;
import jp.green_code.todo.domain.enums.TodoSearchSortEnum;
import jp.green_code.todo.domain.enums.TodoStatusEnum;
import jp.green_code.todo.domain.repository.base.BaseTodoRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Optional.ofNullable;
import static jp.green_code.todo.domain.enums.TodoSearchSortEnum.UPDATE_DESC;
import static jp.green_code.todo.domain.repository.base.BaseTodoRepository.Columns.DEADLINE;
import static jp.green_code.todo.domain.repository.base.BaseTodoRepository.Columns.DETAIL;
import static jp.green_code.todo.domain.repository.base.BaseTodoRepository.Columns.TODO_STATUS;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Table: todo
 */
@Repository
public class TodoRepository extends BaseTodoRepository {
    public TodoRepository(RepositoryHelper helper) {
        super(helper);
    }

    public PageResult<TodoEntity> findByCondition(PageRequest pageable, TodoSearchSortEnum sort, String word, @Nullable TodoStatusEnum status, @Nullable OffsetDateTime deadlineFrom, @Nullable OffsetDateTime deadlineTo) {
        var conditions = new ArrayList<String>();
        if (!isBlank(word)) {
            conditions.add(DETAIL + " like concat('%', :word, '%')");
        }
        if (status != null) {
            conditions.add(TODO_STATUS + " = :status::todo_status");
        }
        if (deadlineFrom != null) {
            conditions.add(":deadlineFrom <= " + DEADLINE);
        }
        if (deadlineTo != null) {
            conditions.add(DEADLINE + " <= :deadlineTo");
        }
        var condition = conditions.isEmpty() ? "" : "where " + String.join("AND ", conditions);

        var param = new HashMap<String, Object>();
        param.put("word", word);
        param.put("status", status + "");
        param.put("deadlineFrom", deadlineFrom);
        param.put("deadlineTo", deadlineTo);
        param.put("limit", pageable.getLimit());
        param.put("offset", pageable.getOffset());

        var sql = new ArrayList<String>();
        sql.add("select count(*)");
        sql.add("from todo");
        sql.add(condition);
        var count = helper.count(sql, param);

        List<TodoEntity> list = List.of();
        if (0 < count) {
            var sb = new ArrayList<String>();
            sb.add("select " + Columns.selectAster());
            sb.add("from todo");
            sb.add(condition);
            sb.add("order by " + ofNullable(sort).orElse(UPDATE_DESC).getSort());
            sb.add("limit :limit offset :offset");
            list = helper.list(sb, param, TodoEntity.class);
        }
        return new PageResult<>(pageable, list, count);
    }
}