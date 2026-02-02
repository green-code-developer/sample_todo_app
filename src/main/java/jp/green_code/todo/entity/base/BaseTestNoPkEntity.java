package jp.green_code.todo.entity.base;

import java.lang.String;

/**
 * Table: test_no_pk
 */
public abstract class BaseTestNoPkEntity {

    /** col_text */
    protected String colText;

    public String getColText() {
        return colText;
    }

    public void setColText(String colText) {
        this.colText = colText;
    }
}