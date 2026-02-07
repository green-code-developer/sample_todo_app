package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.green_code.todo.entity.TodoEntity;

/**
 * Table: todo
 */
public abstract class BaseTodoRepository {

    protected final RepositoryHelper helper;
    public static final String ALL_COLUMNS = "todo_id, todo_status, detail, deadline, updated_at, updated_by, created_at, created_by";

    public static final class Columns {
        public static final String TODO_ID = "todo_id";
        public static final String TODO_STATUS = "todo_status";
        public static final String DETAIL = "detail";
        public static final String DEADLINE = "deadline";
        public static final String UPDATED_AT = "updated_at";
        public static final String UPDATED_BY = "updated_by";
        public static final String CREATED_AT = "created_at";
        public static final String CREATED_BY = "created_by";
        private Columns() {}
    }

    public BaseTodoRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    public Long upsert(TodoEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into todo");
        var insertColumns = new ArrayList<String>();
        if (entity.getTodoId() != null) {
            insertColumns.add("todo_id");
        }
        if (entity.getTodoStatus() != null) {
            insertColumns.add("todo_status");
        }
        if (entity.getDetail() != null) {
            insertColumns.add("detail");
        }
        insertColumns.add("deadline");
        if (entity.getUpdatedAt() != null) {
            insertColumns.add("updated_at");
        }
        if (entity.getUpdatedBy() != null) {
            insertColumns.add("updated_by");
        }
        if (entity.getCreatedAt() != null) {
            insertColumns.add("created_at");
        }
        if (entity.getCreatedBy() != null) {
            insertColumns.add("created_by");
        }
        sql.add("(%s)".formatted(String.join(", ", insertColumns)));
        sql.add("values");
        var insertValues = new ArrayList<String>();
        if (entity.getTodoId() != null) {
            insertValues.add(":todoId");
        }
        if (entity.getTodoStatus() != null) {
            insertValues.add(":todoStatus::todo_status");
        }
        if (entity.getDetail() != null) {
            insertValues.add(":detail");
        }
        insertValues.add(":deadline");
        if (entity.getUpdatedAt() != null) {
            insertValues.add("now()");
        }
        if (entity.getUpdatedBy() != null) {
            insertValues.add(":updatedBy");
        }
        if (entity.getCreatedAt() != null) {
            insertValues.add("now()");
        }
        if (entity.getCreatedBy() != null) {
            insertValues.add(":createdBy");
        }
        sql.add("(%s)".formatted(String.join(", ", insertValues)));
            sql.add("on conflict (");
            sql.add("    todo_id");
        var updateValues = new ArrayList<String>();
        if (entity.getTodoStatus() != null) {
            updateValues.add("todo_status = EXCLUDED.todo_status");
        }
        if (entity.getDetail() != null) {
            updateValues.add("detail = EXCLUDED.detail");
        }
        updateValues.add("deadline = EXCLUDED.deadline");
        if (entity.getUpdatedAt() != null) {
            updateValues.add("updated_at = now()");
        }
        if (entity.getUpdatedBy() != null) {
            updateValues.add("updated_by = EXCLUDED.updated_by");
        }
        if (updateValues.isEmpty()) {
            sql.add(") do nothing");
        } else {
            sql.add(") do update set");
            sql.add(String.join(", ", updateValues));
        }
        sql.add("returning todo_id");

        var param = entityToParam(entity);
        return helper.one(sql, param, Long.class).orElseThrow();
    }

    public static Map<String, Object> entityToParam(TodoEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("todoId", entity.getTodoId());
        param.put("todoStatus", entity.getTodoStatus() + "");
        param.put("detail", entity.getDetail());
        param.put("deadline", entity.getDeadline());
        param.put("updatedAt", entity.getUpdatedAt());
        param.put("updatedBy", entity.getUpdatedBy());
        param.put("createdAt", entity.getCreatedAt());
        param.put("createdBy", entity.getCreatedBy());
        return param;
    }

    public Optional<TodoEntity> findByPk(Long todoId) {
        var sql = new ArrayList<String>();
        sql.add("select %s".formatted(ALL_COLUMNS));
        sql.add("from todo");
        sql.add("where");
        sql.add("    todo_id = :todoId");

        var param = new HashMap<String, Object>();
        param.put("todoId", todoId);

        return helper.one(sql, param, TodoEntity.class);
    }

    public int deleteByPk(Long todoId) {
        var sql = new ArrayList<String>();
        sql.add("delete from todo");
        sql.add("where");
        sql.add("    todo_id = :todoId");

        var param = new HashMap<String, Object>();
        param.put("todoId", todoId);

        return helper.exec(sql, param);
    }
}