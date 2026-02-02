package jp.green_code.todo.entity.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;
import jp.green_code.todo.enums.TodoStatusEnum;

/**
 * Table: todo
 */
public abstract class BaseTodoEntity {

    /** todo_id */
    protected Long todoId;

    /** todo_status */
    protected TodoStatusEnum todoStatus;

    /** detail */
    protected String detail;

    /** deadline */
    protected OffsetDateTime deadline;

    /** updated_at */
    protected OffsetDateTime updatedAt;

    /** updated_by */
    protected Long updatedBy;

    /** created_at */
    protected OffsetDateTime createdAt;

    /** created_by */
    protected Long createdBy;

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public TodoStatusEnum getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(TodoStatusEnum todoStatus) {
        this.todoStatus = todoStatus;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public OffsetDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(OffsetDateTime deadline) {
        this.deadline = deadline;
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