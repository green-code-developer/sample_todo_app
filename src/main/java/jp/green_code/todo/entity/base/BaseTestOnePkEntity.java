package jp.green_code.todo.entity.base;

import java.lang.Long;
import java.lang.String;

/**
 * Table: test_one_pk
 */
public abstract class BaseTestOnePkEntity {

    /** col_bigserial */
    protected Long colBigserial;

    /** col_text */
    protected String colText;

    public Long getColBigserial() {
        return colBigserial;
    }

    public void setColBigserial(Long colBigserial) {
        this.colBigserial = colBigserial;
    }

    public String getColText() {
        return colText;
    }

    public void setColText(String colText) {
        this.colText = colText;
    }
}