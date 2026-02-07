package jp.green_code.todo.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.green_code.todo.entity.TestTriggerUpdateCreateEntity;

/**
 * Table: test_trigger_update_create
 */
public abstract class BaseTestTriggerUpdateCreateRepository {

    protected final RepositoryHelper helper;
    public static final String ALL_COLUMNS = "col_integer_pk1, col_integer_pk2, updated_at, updated_by, created_at, created_by";

    public static final class Columns {
        public static final String COL_INTEGER_PK1 = "col_integer_pk1";
        public static final String COL_INTEGER_PK2 = "col_integer_pk2";
        public static final String UPDATED_AT = "updated_at";
        public static final String UPDATED_BY = "updated_by";
        public static final String CREATED_AT = "created_at";
        public static final String CREATED_BY = "created_by";
        private Columns() {}
    }

    public BaseTestTriggerUpdateCreateRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    public int upsert(TestTriggerUpdateCreateEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into test_trigger_update_create");
        var insertColumns = new ArrayList<String>();
        if (entity.getColIntegerPk1() != null) {
            insertColumns.add("col_integer_pk1");
        }
        if (entity.getColIntegerPk2() != null) {
            insertColumns.add("col_integer_pk2");
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
            if (entity.getColIntegerPk1() != null) {
                insertValues.add(":colIntegerPk1");
            }
            if (entity.getColIntegerPk2() != null) {
                insertValues.add(":colIntegerPk2");
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
                sql.add("    col_integer_pk1,");
                sql.add("    col_integer_pk2");
            var updateValues = new ArrayList<String>();
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
        }

        var param = entityToParam(entity);
        return helper.exec(sql, param);
    }

    public static Map<String, Object> entityToParam(TestTriggerUpdateCreateEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("colIntegerPk1", entity.getColIntegerPk1());
        param.put("colIntegerPk2", entity.getColIntegerPk2());
        param.put("updatedAt", entity.getUpdatedAt());
        param.put("updatedBy", entity.getUpdatedBy());
        param.put("createdAt", entity.getCreatedAt());
        param.put("createdBy", entity.getCreatedBy());
        return param;
    }

    public Optional<TestTriggerUpdateCreateEntity> findByPk(Long colIntegerPk1, Long colIntegerPk2) {
        var sql = new ArrayList<String>();
        sql.add("select %s".formatted(ALL_COLUMNS));
        sql.add("from test_trigger_update_create");
        sql.add("where");
        sql.add("    col_integer_pk1 = :colIntegerPk1 AND");
        sql.add("    col_integer_pk2 = :colIntegerPk2");

        var param = new HashMap<String, Object>();
        param.put("colIntegerPk1", colIntegerPk1);
        param.put("colIntegerPk2", colIntegerPk2);

        return helper.one(sql, param, TestTriggerUpdateCreateEntity.class);
    }

    public int deleteByPk(Long colIntegerPk1, Long colIntegerPk2) {
        var sql = new ArrayList<String>();
        sql.add("delete from test_trigger_update_create");
        sql.add("where");
        sql.add("    col_integer_pk1 = :colIntegerPk1 AND");
        sql.add("    col_integer_pk2 = :colIntegerPk2");

        var param = new HashMap<String, Object>();
        param.put("colIntegerPk1", colIntegerPk1);
        param.put("colIntegerPk2", colIntegerPk2);

        return helper.exec(sql, param);
    }
}