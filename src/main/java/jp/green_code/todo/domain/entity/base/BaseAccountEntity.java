package jp.green_code.todo.domain.entity.base;

import java.time.OffsetDateTime;
import org.jspecify.annotations.Nullable;

/**
 * Table: account
 */
public abstract class BaseAccountEntity {

    /** account_id */
    protected @Nullable Long accountId;

    /** account_status */
    protected String accountStatus = "";

    /** name */
    protected String name = "";

    /** updated_at */
    protected @Nullable OffsetDateTime updatedAt;

    /** updated_by */
    protected long updatedBy = -1L;

    /** created_at */
    protected @Nullable OffsetDateTime createdAt;

    /** created_by */
    protected long createdBy = -1L;

    public @Nullable Long getAccountId() {
        return accountId;
    }

    public void setAccountId(@Nullable Long accountId) {
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

    public @Nullable OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public @Nullable OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }
}