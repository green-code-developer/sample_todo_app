package com.example.todo.entity;

import static com.example.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;
import static com.example.todo.util.DateUtil.BLANK_LOCAL_TIME;

import java.time.LocalDateTime;

public class BaseEntity {

    private LocalDateTime updatedAt = BLANK_LOCAL_TIME;
    private Long updatedBy = SYSTEM_ACCOUNT_ID;
    private LocalDateTime createdAt = BLANK_LOCAL_TIME;
    private Long createdBy = SYSTEM_ACCOUNT_ID;

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
