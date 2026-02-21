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
import jp.green_code.todo.entity.AccountEntity;
import jp.green_code.todo.repository.ColumnDefinition;
import jp.green_code.todo.repository.RepositoryHelper;
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
        public static final ColumnDefinition CREATED_AT = new ColumnDefinition("created_at", "createdAt", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, true);
        public static final ColumnDefinition CREATED_BY = new ColumnDefinition("created_by", "createdBy", "java.lang.Long", "int8", -5, 19, null, false, true, null, null, false, true);

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

    protected List<String> toInsertColumns(AccountEntity entity) {
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

    protected Set<String> toInsertReturning(AccountEntity entity, List<String> insertColumns) {
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
            if (entity.getAccountId() == null) {
                res.add("account_id");
            }
            if (entity.getAccountStatus() == null) {
                res.add("account_status");
            }
            if (entity.getName() == null) {
                res.add("name");
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

    protected List<String> toInsertValues(AccountEntity entity) {
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

    protected void copyReturningValuesInInsert(AccountEntity entity, AccountEntity returning) {
        if (entity.getAccountId() == null) {
            entity.setAccountId(returning.getAccountId());
        }
        if (entity.getAccountStatus() == null) {
            entity.setAccountStatus(returning.getAccountStatus());
        }
        if (entity.getName() == null) {
            entity.setName(returning.getName());
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

    public AccountEntity insert(AccountEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"account\"");
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
            helper.exec(sql, param);
        } else {
            var returningClause = returningColumns.stream().map(c -> Columns.MAP.get(c).toSelectColumn()).collect(joining(", "));
            sql.add("returning %s".formatted(returningClause));
            var ret = helper.single(sql, param, AccountEntity.class);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
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
    public AccountEntity update(AccountEntity entity) {
        return updateByPk(entity, entity.getAccountId());
    }


    protected void copyReturningValuesInUpdate(AccountEntity entity, AccountEntity returning) {
        entity.setUpdatedAt(returning.getUpdatedAt());
    }

    public AccountEntity updateByPk(AccountEntity entity, Long accountId) {
        var sql = new ArrayList<String>();
        sql.add("update \"account\"");
        sql.add("set \"account_id\" = :accountId, \"account_status\" = :accountStatus, \"name\" = :name, \"updated_at\" = now(), \"updated_by\" = :updatedBy, \"created_at\" = now()");
        sql.add("where \"account_id\" = :accountId");
        var param = entityToParam(entity);
        sql.add("returning updated_at");
        var ret = helper.single(sql, param, AccountEntity.class);
        copyReturningValuesInUpdate(entity, ret);
        return entity;
    }

    public Optional<AccountEntity> findByPk(Long accountId) {
        var sql = new ArrayList<String>();
        sql.add("select %s".formatted(Columns.selectAster()));
        sql.add("from \"account\"");
        sql.add("where \"account_id\" = :accountId");

        var param = new HashMap<String, Object>();
        param.put("accountId", accountId);

        return helper.optional(sql, param, AccountEntity.class);
    }

    public int deleteByPk(Long accountId) {
        var sql = new ArrayList<String>();
        sql.add("delete from \"account\"");
        sql.add("where \"account_id\" = :accountId");

        var param = new HashMap<String, Object>();
        param.put("accountId", accountId);

        return helper.exec(sql, param);
    }
}