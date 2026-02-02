package jp.green_code.todo.entity.base;

import java.lang.Long;
import java.lang.String;

/**
 * Table: test_two_pk
 */
public abstract class BaseTestTwoPkEntity {

    /** col_integer_pk1 */
    protected Long colIntegerPk1;

    /** col_integer_pk2 */
    protected Long colIntegerPk2;

    /** col_text */
    protected String colText;

    public Long getColIntegerPk1() {
        return colIntegerPk1;
    }

    public void setColIntegerPk1(Long colIntegerPk1) {
        this.colIntegerPk1 = colIntegerPk1;
    }

    public Long getColIntegerPk2() {
        return colIntegerPk2;
    }

    public void setColIntegerPk2(Long colIntegerPk2) {
        this.colIntegerPk2 = colIntegerPk2;
    }

    public String getColText() {
        return colText;
    }

    public void setColText(String colText) {
        this.colText = colText;
    }
}