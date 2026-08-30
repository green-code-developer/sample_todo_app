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
        public static final ColumnDefinition CREATED_BY = new ColumnDefinition("created_by", "createdBy", "java.lang.Long", "int8", -5, 19, null, false, true, null, null, true, false);

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

    protected List<String> toInsertColumns(AccountEntity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getAccountId() != null) {
            res.add("\"account_id\"");
        }
        if (entity.getAccountStatus() != null) {
            res.add("\"account_status\"");
        }
        if (entity.getName() != null) {
            res.add("\"name\"");
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
            res.add("account_id");
            res.add("account_status");
            res.add("name");
            res.add("updated_at");
            res.add("updated_by");
            res.add("created_at");
            res.add("created_by");
        } else {
            if (!insertColumns.contains("\"account_id\"")) {
                res.add("account_id");
            }
            if (!insertColumns.contains("\"account_status\"")) {
                res.add("account_status");
            }
            if (!insertColumns.contains("\"name\"")) {
                res.add("name");
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

    protected List<String> toInsertValues(AccountEntity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getAccountId() != null) {
            res.add("account_id");
        }
        if (entity.getAccountStatus() != null) {
            res.add("account_status");
        }
        if (entity.getName() != null) {
            res.add("name");
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

    public int insert(AccountEntity entity) {
        return doInsert(entity, false);
    }

    /** 値がnull のカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertNotNull(AccountEntity entity) {
        return doInsert(entity, true);
    }

    protected int doInsert(AccountEntity entity, boolean excludeNull) {
        var __sql = new ArrayList<String>();
        __sql.add("insert into \"account\"");
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

    public int update(AccountEntity entity) {
        return doUpdateByPk(entity, false, entity.getAccountId());
    }

    /** 値がnull のカラムをset 句から外して部分更新する */
    public int updateNotNull(AccountEntity entity) {
        return doUpdateByPk(entity, true, entity.getAccountId());
    }


    public int updateByPk(AccountEntity entity, Long accountId) {
        return doUpdateByPk(entity, false, accountId);
    }

    protected int doUpdateByPk(AccountEntity entity, boolean excludeNull, Long accountId) {
        var __sql = new ArrayList<String>();
        var __param = entityToParam(entity);
        var setClause = Columns.MAP.values().stream()
                .filter(c -> !excludeNull || __param.get(c.getJavaPropertyName()) != null)
                .map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        if (setClause.isEmpty()) {
            throw new IllegalArgumentException("更新対象のカラムがありません");
        }
        __sql.add("update \"account\"");
        __sql.add("set %s".formatted(setClause));
        __param.put("__pk1", accountId);
        __sql.add("where \"account_id\" = :__pk1");
        return execWithReturning(__sql, __param, entity, Set.of("updated_at", "created_at", "created_by"));
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