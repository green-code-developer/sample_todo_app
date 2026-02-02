package jp.green_code.todo.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.green_code.todo.entity.TestNoPkEntity;

/**
 * Table: test_no_pk
 */
public abstract class BaseTestNoPkRepository {

    protected final RepositoryHelper helper;
    public static final String ALL_COLUMNS = "col_text";

    public static final class Columns {
        public static final String COL_TEXT = "col_text";
        private Columns() {}
    }

    public BaseTestNoPkRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    public int upsert(TestNoPkEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into test_no_pk");
        var insertColumns = new ArrayList<String>();
        insertColumns.add("col_text");
        sql.add("(%s)".formatted(String.join(", ", insertColumns)));
        sql.add("values");
        var insertValues = new ArrayList<String>();
        insertValues.add(":colText");
        sql.add("(%s)".formatted(String.join(", ", insertValues)));

        var param = entityToParam(entity);
        return helper.exec(sql, param);
    }

    public static Map<String, Object> entityToParam(TestNoPkEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("colText", entity.getColText());
        return param;
    }
}