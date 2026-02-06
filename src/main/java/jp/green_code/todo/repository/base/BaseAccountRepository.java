package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.green_code.todo.entity.AccountEntity;

/**
 * Table: account
 */
public abstract class BaseAccountRepository {

    protected final RepositoryHelper helper;
    public static final String ALL_COLUMNS = "account_id, account_status, name, updated_at, updated_by, created_at, created_by";

    public static final class Columns {
        public static final String ACCOUNT_ID = "account_id";
        public static final String ACCOUNT_STATUS = "account_status";
        public static final String NAME = "name";
        public static final String UPDATED_AT = "updated_at";
        public static final String UPDATED_BY = "updated_by";
        public static final String CREATED_AT = "created_at";
        public static final String CREATED_BY = "created_by";
        private Columns() {}
    }

    public BaseAccountRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    public Long upsert(AccountEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into account");
        var insertColumns = new ArrayList<String>();
        if (entity.getAccountId() != null) {
            insertColumns.add("account_id");
        }
        if (entity.getAccountStatus() != null) {
            insertColumns.add("account_status");
        }
        if (entity.getName() != null) {
            insertColumns.add("name");
        }
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
        if (insertColumns.isEmpty()) {
            // 全てのカラムがデフォルト値の場合
            sql.add("DEFAULT VALUES");
        } else {
            sql.add("(%s)".formatted(String.join(", ", insertColumns)));
            sql.add("values");
            var insertValues = new ArrayList<String>();
            if (entity.getAccountId() != null) {
                insertValues.add(":accountId");
            }
            if (entity.getAccountStatus() != null) {
                insertValues.add(":accountStatus");
            }
            if (entity.getName() != null) {
                insertValues.add(":name");
            }
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
            sql.add("    account_id");
            sql.add(") do update set");
            var updateValues = new ArrayList<String>();
            if (entity.getAccountStatus() != null) {
                updateValues.add("account_status = EXCLUDED.account_status");
            }
            if (entity.getName() != null) {
                updateValues.add("name = EXCLUDED.name");
            }
            if (entity.getUpdatedAt() != null) {
                updateValues.add("updated_at = now()");
            }
            if (entity.getUpdatedBy() != null) {
                updateValues.add("updated_by = EXCLUDED.updated_by");
            }
            sql.add(String.join(", ", updateValues));
        }
        sql.add("returning account_id");

        var param = entityToParam(entity);
        return helper.one(sql, param, Long.class).orElseThrow();
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

    public Optional<AccountEntity> findByPk(Long accountId) {
        var sql = new ArrayList<String>();
        sql.add("select %s".formatted(ALL_COLUMNS));
        sql.add("from account");
        sql.add("where");
        sql.add("    account_id = :accountId");

        var param = new HashMap<String, Object>();
        param.put("accountId", accountId);

        return helper.one(sql, param, AccountEntity.class);
    }

    public int deleteByPk(Long accountId) {
        var sql = new ArrayList<String>();
        sql.add("delete from account");
        sql.add("where");
        sql.add("    account_id = :accountId");

        var param = new HashMap<String, Object>();
        param.put("accountId", accountId);

        return helper.exec(sql, param);
    }
}