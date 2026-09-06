package jp.green_code.todo.domain.dto;

import org.jspecify.annotations.Nullable;

/**
 * Todo 登録・更新フォーム
 */
public class TodoDto {
    // 新規の場合はnull
    @Nullable
    private Long todoId;

    @Nullable
    private String detail;

    @Nullable
    private String deadline;

    @Nullable
    private String todoStatus;
    public boolean isNew() {
        return todoId == null;
    }

    public String toLogString() {
        return String.format("TodoForm(todoId=%s,detail=%s,deadlineFrom=%s,todoStatus=%s)", todoId, detail, deadline, todoStatus);
    }

    public @Nullable Long getTodoId() {
        return todoId;
    }

    public void setTodoId(@Nullable Long todoId) {
        this.todoId = todoId;
    }

    public @Nullable String getDetail() {
        return detail;
    }

    public void setDetail(@Nullable String detail) {
        this.detail = detail;
    }

    public @Nullable String getDeadline() {
        return deadline;
    }

    public void setDeadline(@Nullable String deadline) {
        this.deadline = deadline;
    }

    public @Nullable String getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(@Nullable String todoStatus) {
        this.todoStatus = todoStatus;
    }
}
