package jp.green_code.todo.domain.repository.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import jp.green_code.todo.domain.entity.AccountEntity;
import jp.green_code.todo.domain.repository.ColumnDefinition;
import jp.green_code.todo.domain.repository.RepositoryHelper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: account
 */
public abstract class BaseAccountRepository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition ACCOUNT_ID = new ColumnDefinition("account_id", "accountId", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false);
        public static final ColumnDefinition ACCOUNT_STATUS = new ColumnDefinition("account_status", "accountStatus", "java.lang.String", "varchar", 12, 2147483647, null, false, true, null, null, false, false);
        public static final ColumnDefinition NAME = new ColumnDefinition("name", "name", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false);
        public static final ColumnDefinition UPDATED_AT = new ColumnDefinition("updated_at", "updatedAt", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, false);
        public static final ColumnDefinition UPDATED_BY = new ColumnDefinition("updated_by", "updatedBy", "java.lang.Long", "int8", -5, 19, null, false, true, null, null, false, false);
        public static final ColumnDefinition CREATED_AT = new ColumnDefinition("created_at", "createdAt", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, false);
        public static final ColumnDefinition CREATED_BY = new ColumnDefinition("created_by", "createdBy", "java.lang.Long", "int8", -5, 19, null, false, true, null, null, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("account_id", ACCOUNT_ID);
            MAP.put("account_status", ACCOUNT_STATUS);
            MAP.put("name", NAME);
            MAP.put("updated_at", UPDATED_AT);
            MAP.put("updated_by", UPDATED_BY);
            MAP.put("created_at", CREATED_AT);
            MAP.put("created_by", CREATED_BY);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseAccountRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    /** 他テーブルのカラム、重複指定、PK 指定を弾く */
    protected List<ColumnDefinition> validateColumns(ColumnDefinition first, ColumnDefinition[] rest, boolean rejectPk) {
        var columns = new ArrayList<ColumnDefinition>();
        columns.add(first);
        columns.addAll(Arrays.asList(rest));
        var names = new HashSet<String>();
        for (var c : columns) {
            if (rejectPk && c.getPrimaryKeySeq() != null) {
                throw new IllegalArgumentException("PK は指定できません: " + c.getColumnName());
            }
            if (Columns.MAP.get(c.getColumnName()) != c) {
                throw new IllegalArgumentException("account のカラムではありません: " + c.getColumnName());
            }
            if (!names.add(c.getColumnName())) {
                throw new IllegalArgumentException("カラムが重複しています: " + c.getColumnName());
            }
        }
        return columns;
    }

    protected List<String> toInsertColumns(Set<String> excludeColumns) {
        return Columns.MAP.values().stream()
                .filter(c -> !excludeColumns.contains(c.getColumnName()))
                .map(c -> "\"%s\"".formatted(c.getColumnName()))
                .toList();
    }

    protected List<String> toInsertValues(Set<String> excludeColumns) {
        return Columns.MAP.values().stream()
                .filter(c -> !excludeColumns.contains(c.getColumnName()))
                .map(ColumnDefinition::toParamColumn)
                .toList();
    }

    protected Set<String> toInsertReturning(Set<String> excludeColumns) {
        var res = new LinkedHashSet<String>();
        for (var c : Columns.MAP.values()) {
            if (c.isReturning() || excludeColumns.contains(c.getColumnName())) {
                res.add(c.getColumnName());
            }
        }
        return res;
    }

    protected void copyReturningValues(AccountEntity entity, AccountEntity returning, Set<String> returningColumns) {
        if (returningColumns.contains("account_id")) {
            entity.setAccountId(returning.getAccountId());
        }
        if (returningColumns.contains("account_status")) {
            entity.setAccountStatus(returning.getAccountStatus());
        }
        if (returningColumns.contains("name")) {
            entity.setName(returning.getName());
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

    protected int execWithReturning(List<String> sql, Map<String, Object> param, AccountEntity entity, Set<String> returningColumns) {
        if (returningColumns.isEmpty()) {
            return this.helper.exec(sql, param);
        }
        var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        sql.add("returning %s".formatted(returningClause));
        var ret = this.helper.optional(sql, param, AccountEntity.class);
        ret.ifPresent(r -> copyReturningValues(entity, r, returningColumns));
        return ret.isPresent() ? 1 : 0;
    }

    /** 全カラムをINSERT 対象とする */
    public int insertAllColumns(AccountEntity entity) {
        return doInsert(entity, Set.of());
    }

    /** 指定したカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertExcept(AccountEntity entity, ColumnDefinition first, ColumnDefinition... rest) {
        var exclude = new LinkedHashSet<String>();
        validateColumns(first, rest, false).forEach(c -> exclude.add(c.getColumnName()));
        return doInsert(entity, exclude);
    }

    /** PK をINSERT 対象から外し、DB に値を決めさせる */
    public int insertExceptPk(AccountEntity entity) {
        return insertExcept(entity, Columns.ACCOUNT_ID);
    }

    protected int doInsert(AccountEntity entity, Set<String> excludeColumns) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"account\"");
        var insertColumns = toInsertColumns(excludeColumns);
        if (insertColumns.isEmpty()) {
            sql.add("DEFAULT VALUES");
        } else {
            sql.add("(%s)".formatted(join(", ", insertColumns)));
            sql.add("values (%s)".formatted(join(", ", toInsertValues(excludeColumns))));
        }
        var param = entityToParam(entity);
        return execWithReturning(sql, param, entity, toInsertReturning(excludeColumns));
    }

    public static Map<String, Object> entityToParam(AccountEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("accountId", entity.getAccountId());
        param.put("accountStatus", entity.getAccountStatus());
        param.put("name", entity.getName());
        param.put("updatedAt", entity.getUpdatedAt());
        param.put("updatedBy", entity.getUpdatedBy());
        param.put("createdAt", entity.getCreatedAt());
        param.put("createdBy", entity.getCreatedBy());
        return param;
    }

    /** PK を除く全カラムを更新する */
    public int updateAllColumns(AccountEntity entity) {
        return doUpdate(entity, Columns.MAP.values().stream().filter(c -> c.getPrimaryKeySeq() == null).toList());
    }

    /** 指定したカラムだけを更新する */
    public int updateInclude(AccountEntity entity, ColumnDefinition first, ColumnDefinition... rest) {
        return doUpdate(entity, validateColumns(first, rest, true));
    }

    protected int doUpdate(AccountEntity entity, List<ColumnDefinition> setColumns) {
        var sql = new ArrayList<String>();
        var param = entityToParam(entity);
        var setClause = setColumns.stream().map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        sql.add("update \"account\"");
        sql.add("set %s".formatted(setClause));
        param.put("__pk1", entity.getAccountId());
        sql.add("where \"account_id\" = :__pk1");
        return execWithReturning(sql, param, entity, Set.of("updated_at", "created_at"));
    }

    public Optional<AccountEntity> findByPk(Long accountId) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"account\"");
        __sql.add("where \"account_id\" = :accountId");

        var __param = new HashMap<String, Object>();
        __param.put("accountId", accountId);

        return this.helper.optional(__sql, __param, AccountEntity.class);
    }

    public int deleteByPk(Long accountId) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"account\"");
        __sql.add("where \"account_id\" = :accountId");

        var __param = new HashMap<String, Object>();
        __param.put("accountId", accountId);

        return this.helper.exec(__sql, __param);
    }
}