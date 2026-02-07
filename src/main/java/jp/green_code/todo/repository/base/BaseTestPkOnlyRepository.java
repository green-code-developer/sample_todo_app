package jp.green_code.todo.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.green_code.todo.entity.TestPkOnlyEntity;

/**
 * Table: test_pk_only
 */
public abstract class BaseTestPkOnlyRepository {

    protected final RepositoryHelper helper;
    public static final String ALL_COLUMNS = "col_integer_pk1, col_integer_pk2";

    public static final class Columns {
        public static final String COL_INTEGER_PK1 = "col_integer_pk1";
        public static final String COL_INTEGER_PK2 = "col_integer_pk2";
        private Columns() {}
    }

    public BaseTestPkOnlyRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    public int upsert(TestPkOnlyEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into test_pk_only");
        var insertColumns = new ArrayList<String>();
        if (entity.getColIntegerPk1() != null) {
            insertColumns.add("col_integer_pk1");
        }
        if (entity.getColIntegerPk2() != null) {
            insertColumns.add("col_integer_pk2");
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
            sql.add("(%s)".formatted(String.join(", ", insertValues)));
                sql.add("on conflict (");
                sql.add("    col_integer_pk1,");
                sql.add("    col_integer_pk2");
            var updateValues = new ArrayList<String>();
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

    public static Map<String, Object> entityToParam(TestPkOnlyEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("colIntegerPk1", entity.getColIntegerPk1());
        param.put("colIntegerPk2", entity.getColIntegerPk2());
        return param;
    }

    public Optional<TestPkOnlyEntity> findByPk(Long colIntegerPk1, Long colIntegerPk2) {
        var sql = new ArrayList<String>();
        sql.add("select %s".formatted(ALL_COLUMNS));
        sql.add("from test_pk_only");
        sql.add("where");
        sql.add("    col_integer_pk1 = :colIntegerPk1 AND");
        sql.add("    col_integer_pk2 = :colIntegerPk2");

        var param = new HashMap<String, Object>();
        param.put("colIntegerPk1", colIntegerPk1);
        param.put("colIntegerPk2", colIntegerPk2);

        return helper.one(sql, param, TestPkOnlyEntity.class);
    }

    public int deleteByPk(Long colIntegerPk1, Long colIntegerPk2) {
        var sql = new ArrayList<String>();
        sql.add("delete from test_pk_only");
        sql.add("where");
        sql.add("    col_integer_pk1 = :colIntegerPk1 AND");
        sql.add("    col_integer_pk2 = :colIntegerPk2");

        var param = new HashMap<String, Object>();
        param.put("colIntegerPk1", colIntegerPk1);
        param.put("colIntegerPk2", colIntegerPk2);

        return helper.exec(sql, param);
    }
}