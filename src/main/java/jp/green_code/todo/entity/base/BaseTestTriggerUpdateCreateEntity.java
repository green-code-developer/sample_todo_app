package jp.green_code.todo.entity.base;

import java.lang.Long;
import java.time.OffsetDateTime;

/**
 * Table: test_trigger_update_create
 */
public abstract class BaseTestTriggerUpdateCreateEntity {

    /** col_integer_pk1 */
    protected Long colIntegerPk1;

    /** col_integer_pk2 */
    protected Long colIntegerPk2;

    /** updated_at */
    protected OffsetDateTime updatedAt;

    /** updated_by */
    protected Long updatedBy;

    /** created_at */
    protected OffsetDateTime createdAt;

    /** created_by */
    protected Long createdBy;

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

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}