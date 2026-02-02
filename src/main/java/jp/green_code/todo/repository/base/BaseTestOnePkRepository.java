package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.green_code.todo.entity.TestOnePkEntity;

/**
 * Table: test_one_pk
 */
public abstract class BaseTestOnePkRepository {

    protected final RepositoryHelper helper;
    public static final String ALL_COLUMNS = "col_bigserial, col_text";

    public static final class Columns {
        public static final String COL_BIGSERIAL = "col_bigserial";
        public static final String COL_TEXT = "col_text";
        private Columns() {}
    }

    public BaseTestOnePkRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    public Long upsert(TestOnePkEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into test_one_pk");
        var insertColumns = new ArrayList<String>();
        if (entity.getColBigserial() != null) {
            insertColumns.add("col_bigserial");
        }
        insertColumns.add("col_text");
        sql.add("(%s)".formatted(String.join(", ", insertColumns)));
        sql.add("values");
        var insertValues = new ArrayList<String>();
        if (entity.getColBigserial() != null) {
            insertValues.add(":colBigserial");
        }
        insertValues.add(":colText");
        sql.add("(%s)".formatted(String.join(", ", insertValues)));
        sql.add("on conflict (");
        sql.add("    col_bigserial");
        sql.add(") do update set");
        var updateValues = new ArrayList<String>();
        updateValues.add("col_text = EXCLUDED.col_text");
        sql.add(String.join(", ", updateValues));
        sql.add("returning col_bigserial");

        var param = entityToParam(entity);
        return helper.one(sql, param, Long.class).orElseThrow();
    }

    public static Map<String, Object> entityToParam(TestOnePkEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("colBigserial", entity.getColBigserial());
        param.put("colText", entity.getColText());
        return param;
    }

    public Optional<TestOnePkEntity> findByPk(Long colBigserial) {
        var sql = new ArrayList<String>();
        sql.add("select %s".formatted(ALL_COLUMNS));
        sql.add("from test_one_pk");
        sql.add("where");
        sql.add("    col_bigserial = :colBigserial");

        var param = new HashMap<String, Object>();
        param.put("colBigserial", colBigserial);

        return helper.one(sql, param, TestOnePkEntity.class);
    }

    public int deleteByPk(Long colBigserial) {
        var sql = new ArrayList<String>();
        sql.add("delete from test_one_pk");
        sql.add("where");
        sql.add("    col_bigserial = :colBigserial");

        var param = new HashMap<String, Object>();
        param.put("colBigserial", colBigserial);

        return helper.exec(sql, param);
    }
}