package jp.green_code.todo.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.green_code.todo.entity.TestTwoPkEntity;

/**
 * Table: test_two_pk
 */
public abstract class BaseTestTwoPkRepository {

    protected final RepositoryHelper helper;
    public static final String ALL_COLUMNS = "col_integer_pk1, col_integer_pk2, col_text";

    public static final class Columns {
        public static final String COL_INTEGER_PK1 = "col_integer_pk1";
        public static final String COL_INTEGER_PK2 = "col_integer_pk2";
        public static final String COL_TEXT = "col_text";
        private Columns() {}
    }

    public BaseTestTwoPkRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    public int upsert(TestTwoPkEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into test_two_pk");
        var insertColumns = new ArrayList<String>();
        if (entity.getColIntegerPk1() != null) {
            insertColumns.add("col_integer_pk1");
        }
        if (entity.getColIntegerPk2() != null) {
            insertColumns.add("col_integer_pk2");
        }
        insertColumns.add("col_text");
        sql.add("(%s)".formatted(String.join(", ", insertColumns)));
        sql.add("values");
        var insertValues = new ArrayList<String>();
        if (entity.getColIntegerPk1() != null) {
            insertValues.add(":colIntegerPk1");
        }
        if (entity.getColIntegerPk2() != null) {
            insertValues.add(":colIntegerPk2");
        }
        insertValues.add(":colText");
        sql.add("(%s)".formatted(String.join(", ", insertValues)));
        sql.add("on conflict (");
        sql.add("    col_integer_pk1,");
        sql.add("    col_integer_pk2");
        sql.add(") do update set");
        var updateValues = new ArrayList<String>();
        updateValues.add("col_text = EXCLUDED.col_text");
        sql.add(String.join(", ", updateValues));

        var param = entityToParam(entity);
        return helper.exec(sql, param);
    }

    public static Map<String, Object> entityToParam(TestTwoPkEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("colIntegerPk1", entity.getColIntegerPk1());
        param.put("colIntegerPk2", entity.getColIntegerPk2());
        param.put("colText", entity.getColText());
        return param;
    }

    public Optional<TestTwoPkEntity> findByPk(Long colIntegerPk1, Long colIntegerPk2) {
        var sql = new ArrayList<String>();
        sql.add("select %s".formatted(ALL_COLUMNS));
        sql.add("from test_two_pk");
        sql.add("where");
        sql.add("    col_integer_pk1 = :colIntegerPk1 AND");
        sql.add("    col_integer_pk2 = :colIntegerPk2");

        var param = new HashMap<String, Object>();
        param.put("colIntegerPk1", colIntegerPk1);
        param.put("colIntegerPk2", colIntegerPk2);

        return helper.one(sql, param, TestTwoPkEntity.class);
    }

    public int deleteByPk(Long colIntegerPk1, Long colIntegerPk2) {
        var sql = new ArrayList<String>();
        sql.add("delete from test_two_pk");
        sql.add("where");
        sql.add("    col_integer_pk1 = :colIntegerPk1 AND");
        sql.add("    col_integer_pk2 = :colIntegerPk2");

        var param = new HashMap<String, Object>();
        param.put("colIntegerPk1", colIntegerPk1);
        param.put("colIntegerPk2", colIntegerPk2);

        return helper.exec(sql, param);
    }
}