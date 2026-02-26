package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import jp.green_code.todo.entity.TodoEntity;
import jp.green_code.todo.repository.ColumnDefinition;
import jp.green_code.todo.repository.RepositoryHelper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: todo
 */
public abstract class BaseTodoRepository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition TODO_ID = new ColumnDefinition("todo_id", "todoId", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false, false);
        public static final ColumnDefinition TODO_STATUS = new ColumnDefinition("todo_status", "todoStatus", "jp.green_code.todo.enums.TodoStatusEnum", "todo_status", 12, 2147483647, null, false, true, ":{javaPropertyName}::todo_status", null, false, false, false);
        public static final ColumnDefinition DETAIL = new ColumnDefinition("detail", "detail", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false, false);
        public static final ColumnDefinition DEADLINE = new ColumnDefinition("deadline", "deadline", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, true, false, null, null, false, false, false);
        public static final ColumnDefinition UPDATED_AT = new ColumnDefinition("updated_at", "updatedAt", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, false, false);
        public static final ColumnDefinition UPDATED_BY = new ColumnDefinition("updated_by", "updatedBy", "java.lang.Long", "int8", -5, 19, null, false, true, null, null, false, false, false);
        public static final ColumnDefinition CREATED_AT = new ColumnDefinition("created_at", "createdAt", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, true, false);
        public static final ColumnDefinition CREATED_BY = new ColumnDefinition("created_by", "createdBy", "java.lang.Long", "int8", -5, 19, null, false, true, null, null, false, true, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("todo_id", TODO_ID);
            MAP.put("todo_status", TODO_STATUS);
            MAP.put("detail", DETAIL);
            MAP.put("deadline", DEADLINE);
            MAP.put("updated_at", UPDATED_AT);
            MAP.put("updated_by", UPDATED_BY);
            MAP.put("created_at", CREATED_AT);
            MAP.put("created_by", CREATED_BY);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseTodoRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(TodoEntity entity) {
        var res = new ArrayList<String>();
        if (entity.getTodoId() != null) {
            res.add("\"todo_id\"");
        }
        if (entity.getTodoStatus() != null) {
            res.add("\"todo_status\"");
        }
        if (entity.getDetail() != null) {
            res.add("\"detail\"");
        }
        res.add("\"deadline\"");
        res.add("\"updated_at\"");
        if (entity.getUpdatedBy() != null) {
            res.add("\"updated_by\"");
        }
        res.add("\"created_at\"");
        if (entity.getCreatedBy() != null) {
            res.add("\"created_by\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(TodoEntity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("todo_id");
            res.add("todo_status");
            res.add("detail");
            res.add("deadline");
            res.add("updated_at");
            res.add("updated_by");
            res.add("created_at");
            res.add("created_by");
        } else {
            if (entity.getTodoId() == null) {
                res.add("todo_id");
            }
            if (entity.getTodoStatus() == null) {
                res.add("todo_status");
            }
            if (entity.getDetail() == null) {
                res.add("detail");
            }
            res.add("updated_at");
            if (entity.getUpdatedBy() == null) {
                res.add("updated_by");
            }
            res.add("created_at");
            if (entity.getCreatedBy() == null) {
                res.add("created_by");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(TodoEntity entity) {
        var res = new ArrayList<String>();
        if (entity.getTodoId() != null) {
            res.add("todo_id");
        }
        if (entity.getTodoStatus() != null) {
            res.add("todo_status");
        }
        if (entity.getDetail() != null) {
            res.add("detail");
        }
        res.add("deadline");
        res.add("now()");
        if (entity.getUpdatedBy() != null) {
            res.add("updated_by");
        }
        res.add("now()");
        if (entity.getCreatedBy() != null) {
            res.add("created_by");
        }
        return res;
    }

    protected void copyReturningValuesInInsert(TodoEntity entity, TodoEntity returning) {
        if (entity.getTodoId() == null) {
            entity.setTodoId(returning.getTodoId());
        }
        if (entity.getTodoStatus() == null) {
            entity.setTodoStatus(returning.getTodoStatus());
        }
        if (entity.getDetail() == null) {
            entity.setDetail(returning.getDetail());
        }
        entity.setUpdatedAt(returning.getUpdatedAt());
        if (entity.getUpdatedBy() == null) {
            entity.setUpdatedBy(returning.getUpdatedBy());
        }
        entity.setCreatedAt(returning.getCreatedAt());
        if (entity.getCreatedBy() == null) {
            entity.setCreatedBy(returning.getCreatedBy());
        }
    }

    public TodoEntity insert(TodoEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"todo\"");
        var insertColumns = toInsertColumns(entity);
        if (insertColumns.isEmpty()) {
            sql.add("DEFAULT VALUES");
        } else {
            sql.add("(%s)".formatted(join(", ", insertColumns)));
            var insertValues = toInsertValues(entity);
            var insertValuesClause = insertValues.stream().map(c -> Columns.MAP.get(c) == null ? c : Columns.MAP.get(c).toParamColumn()).collect(joining(", "));
            sql.add("values (%s)".formatted(insertValuesClause));
        }
        var param = entityToParam(entity);
        var returningColumns = toInsertReturning(entity, insertColumns);
        if (returningColumns.isEmpty()) {
            this.helper.exec(sql, param);
        } else {
            var returningClause = returningColumns.stream().map(c -> Columns.MAP.get(c).toSelectColumn()).collect(joining(", "));
            sql.add("returning %s".formatted(returningClause));
            var ret = this.helper.single(sql, param, TodoEntity.class);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
    }

    public static Map<String, Object> entityToParam(TodoEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("todoId", entity.getTodoId());
        param.put("todoStatus", String.valueOf(entity.getTodoStatus()));
        param.put("detail", entity.getDetail());
        param.put("deadline", entity.getDeadline());
        param.put("updatedAt", entity.getUpdatedAt());
        param.put("updatedBy", entity.getUpdatedBy());
        param.put("createdAt", entity.getCreatedAt());
        param.put("createdBy", entity.getCreatedBy());
        return param;
    }

    public TodoEntity update(TodoEntity entity) {
        return updateByPk(entity, entity.getTodoId());
    }

    protected void copyReturningValuesInUpdate(TodoEntity entity, TodoEntity returning) {
        entity.setUpdatedAt(returning.getUpdatedAt());
    }

    public TodoEntity updateByPk(TodoEntity entity, Long todoId) {
        var __sql = new ArrayList<String>();
        var setClause = Columns.MAP.values().stream().filter(c-> !c.isShouldSkipInUpdate()).map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        __sql.add("update \"todo\"");
        __sql.add("set %s".formatted(setClause));
        var __param = entityToParam(entity);
        __param.put("__pk1", todoId);
        __sql.add("where \"todo_id\" = :__pk1");
        __sql.add("returning updated_at");
        var ret = this.helper.single(__sql, __param, TodoEntity.class);
        copyReturningValuesInUpdate(entity, ret);
        return entity;
    }

    public Optional<TodoEntity> findByPk(Long todoId) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"todo\"");
        __sql.add("where \"todo_id\" = :todoId");

        var __param = new HashMap<String, Object>();
        __param.put("todoId", todoId);

        return this.helper.optional(__sql, __param, TodoEntity.class);
    }

    public int deleteByPk(Long todoId) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"todo\"");
        __sql.add("where \"todo_id\" = :todoId");

        var __param = new HashMap<String, Object>();
        __param.put("todoId", todoId);

        return this.helper.exec(__sql, __param);
    }
}