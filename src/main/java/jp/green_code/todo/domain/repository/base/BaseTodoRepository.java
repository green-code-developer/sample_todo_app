package jp.green_code.todo.domain.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import jp.green_code.todo.domain.entity.TodoEntity;
import jp.green_code.todo.domain.repository.ColumnDefinition;
import jp.green_code.todo.domain.repository.RepositoryHelper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: todo
 */
public abstract class BaseTodoRepository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition TODO_ID = new ColumnDefinition("todo_id", "todoId", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false);
        public static final ColumnDefinition TODO_STATUS = new ColumnDefinition("todo_status", "todoStatus", "jp.green_code.todo.domain.enums.TodoStatusEnum", "todo_status", 12, 2147483647, null, false, true, ":{javaPropertyName}::todo_status", null, false, false);
        public static final ColumnDefinition DETAIL = new ColumnDefinition("detail", "detail", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false);
        public static final ColumnDefinition DEADLINE = new ColumnDefinition("deadline", "deadline", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, true, false, null, null, false, false);
        public static final ColumnDefinition UPDATED_AT = new ColumnDefinition("updated_at", "updatedAt", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, false);
        public static final ColumnDefinition UPDATED_BY = new ColumnDefinition("updated_by", "updatedBy", "java.lang.Long", "int8", -5, 19, null, false, true, null, null, false, false);
        public static final ColumnDefinition CREATED_AT = new ColumnDefinition("created_at", "createdAt", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, false);
        public static final ColumnDefinition CREATED_BY = new ColumnDefinition("created_by", "createdBy", "java.lang.Long", "int8", -5, 19, null, false, true, null, null, true, false);

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

    protected List<String> toInsertColumns(TodoEntity entity, boolean excludeNull) {
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
        if (!excludeNull || entity.getDeadline() != null) {
            res.add("\"deadline\"");
        }
        if (entity.getUpdatedAt() != null) {
            res.add("\"updated_at\"");
        }
        if (entity.getUpdatedBy() != null) {
            res.add("\"updated_by\"");
        }
        if (entity.getCreatedAt() != null) {
            res.add("\"created_at\"");
        }
        if (entity.getCreatedBy() != null) {
            res.add("\"created_by\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(List<String> insertColumns) {
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
            if (!insertColumns.contains("\"todo_id\"")) {
                res.add("todo_id");
            }
            if (!insertColumns.contains("\"todo_status\"")) {
                res.add("todo_status");
            }
            if (!insertColumns.contains("\"detail\"")) {
                res.add("detail");
            }
            if (!insertColumns.contains("\"deadline\"")) {
                res.add("deadline");
            }
            res.add("updated_at");
            if (!insertColumns.contains("\"updated_by\"")) {
                res.add("updated_by");
            }
            res.add("created_at");
            res.add("created_by");
        }
        return res;
    }

    protected List<String> toInsertValues(TodoEntity entity, boolean excludeNull) {
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
        if (!excludeNull || entity.getDeadline() != null) {
            res.add("deadline");
        }
        if (entity.getUpdatedAt() != null) {
            res.add("updated_at");
        }
        if (entity.getUpdatedBy() != null) {
            res.add("updated_by");
        }
        if (entity.getCreatedAt() != null) {
            res.add("created_at");
        }
        if (entity.getCreatedBy() != null) {
            res.add("created_by");
        }
        return res;
    }

    protected void copyReturningValues(TodoEntity entity, TodoEntity returning, Set<String> returningColumns) {
        if (returningColumns.contains("todo_id")) {
            entity.setTodoId(returning.getTodoId());
        }
        if (returningColumns.contains("todo_status")) {
            entity.setTodoStatus(returning.getTodoStatus());
        }
        if (returningColumns.contains("detail")) {
            entity.setDetail(returning.getDetail());
        }
        if (returningColumns.contains("deadline")) {
            entity.setDeadline(returning.getDeadline());
        }
        if (returningColumns.contains("updated_at")) {
            entity.setUpdatedAt(returning.getUpdatedAt());
        }
        if (returningColumns.contains("updated_by")) {
            entity.setUpdatedBy(returning.getUpdatedBy());
        }
        if (returningColumns.contains("created_at")) {
            entity.setCreatedAt(returning.getCreatedAt());
        }
        if (returningColumns.contains("created_by")) {
            entity.setCreatedBy(returning.getCreatedBy());
        }
    }

    protected int execWithReturning(List<String> sql, Map<String, Object> param, TodoEntity entity, Set<String> returningColumns) {
        if (returningColumns.isEmpty()) {
            return this.helper.exec(sql, param);
        }
        var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        sql.add("returning %s".formatted(returningClause));
        var ret = this.helper.optional(sql, param, TodoEntity.class);
        ret.ifPresent(r -> copyReturningValues(entity, r, returningColumns));
        return ret.isPresent() ? 1 : 0;
    }

    public int insert(TodoEntity entity) {
        return doInsert(entity, false);
    }

    /** 値がnull のカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertNotNull(TodoEntity entity) {
        return doInsert(entity, true);
    }

    protected int doInsert(TodoEntity entity, boolean excludeNull) {
        var __sql = new ArrayList<String>();
        __sql.add("insert into \"todo\"");
        var __insertColumns = toInsertColumns(entity, excludeNull);
        if (__insertColumns.isEmpty()) {
            __sql.add("DEFAULT VALUES");
        } else {
            __sql.add("(%s)".formatted(join(", ", __insertColumns)));
            var __insertValues = toInsertValues(entity, excludeNull);
            var __valuesClause = __insertValues.stream().map(c -> Columns.MAP.get(c) == null ? c : Columns.MAP.get(c).toParamColumn()).collect(joining(", "));
            __sql.add("values (%s)".formatted(__valuesClause));
        }
        var __param = entityToParam(entity);
        return execWithReturning(__sql, __param, entity, toInsertReturning(__insertColumns));
    }

    public static Map<String, Object> entityToParam(TodoEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("todoId", entity.getTodoId());
        param.put("todoStatus", entity.getTodoStatus() == null ? null : entity.getTodoStatus().name());
        param.put("detail", entity.getDetail());
        param.put("deadline", entity.getDeadline());
        param.put("updatedAt", entity.getUpdatedAt());
        param.put("updatedBy", entity.getUpdatedBy());
        param.put("createdAt", entity.getCreatedAt());
        param.put("createdBy", entity.getCreatedBy());
        return param;
    }

    public int update(TodoEntity entity) {
        return doUpdateByPk(entity, false, entity.getTodoId());
    }

    /** 値がnull のカラムをset 句から外して部分更新する */
    public int updateNotNull(TodoEntity entity) {
        return doUpdateByPk(entity, true, entity.getTodoId());
    }


    public int updateByPk(TodoEntity entity, Long todoId) {
        return doUpdateByPk(entity, false, todoId);
    }

    protected int doUpdateByPk(TodoEntity entity, boolean excludeNull, Long todoId) {
        var __sql = new ArrayList<String>();
        var __param = entityToParam(entity);
        var setClause = Columns.MAP.values().stream()
                .filter(c -> !excludeNull || __param.get(c.getJavaPropertyName()) != null)
                .map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        if (setClause.isEmpty()) {
            throw new IllegalArgumentException("更新対象のカラムがありません");
        }
        __sql.add("update \"todo\"");
        __sql.add("set %s".formatted(setClause));
        __param.put("__pk1", todoId);
        __sql.add("where \"todo_id\" = :__pk1");
        return execWithReturning(__sql, __param, entity, Set.of("updated_at", "created_at", "created_by"));
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