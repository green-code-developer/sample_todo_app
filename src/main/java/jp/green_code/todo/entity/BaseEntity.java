package jp.green_code.todo.entity;

import static jp.green_code.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;

import java.time.OffsetDateTime;
import jp.green_code.todo.util.DateUtil;

public class BaseEntity {

    private OffsetDateTime updatedAt = DateUtil.BLANK_OFFSET_TIME;
    private Long updatedBy = SYSTEM_ACCOUNT_ID;
    private OffsetDateTime createdAt = DateUtil.BLANK_OFFSET_TIME;
    private Long createdBy = SYSTEM_ACCOUNT_ID;

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
