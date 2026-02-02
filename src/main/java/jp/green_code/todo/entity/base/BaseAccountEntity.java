package jp.green_code.todo.entity.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;

/**
 * Table: account
 */
public abstract class BaseAccountEntity {

    /** account_id */
    protected Long accountId;

    /** account_status */
    protected String accountStatus;

    /** name */
    protected String name;

    /** updated_at */
    protected OffsetDateTime updatedAt;

    /** updated_by */
    protected Long updatedBy;

    /** created_at */
    protected OffsetDateTime createdAt;

    /** created_by */
    protected Long createdBy;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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