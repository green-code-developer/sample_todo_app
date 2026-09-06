package jp.green_code.todo.domain.entity.base;

import java.time.OffsetDateTime;
import jp.green_code.todo.domain.enums.TodoStatusEnum;
import org.jspecify.annotations.Nullable;

/**
 * Table: todo
 */
public abstract class BaseTodoEntity {

    /** todo_id */
    protected @Nullable Long todoId;

    /** todo_status */
    protected TodoStatusEnum todoStatus = TodoStatusEnum.NEW;

    /** detail */
    protected String detail = "";

    /** deadline */
    protected @Nullable OffsetDateTime deadline;

    /** updated_at */
    protected @Nullable OffsetDateTime updatedAt;

    /** updated_by */
    protected long updatedBy = -1L;

    /** created_at */
    protected @Nullable OffsetDateTime createdAt;

    /** created_by */
    protected long createdBy = -1L;

    public @Nullable Long getTodoId() {
        return todoId;
    }

    public void setTodoId(@Nullable Long todoId) {
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

    public @Nullable OffsetDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(@Nullable OffsetDateTime deadline) {
        this.deadline = deadline;
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