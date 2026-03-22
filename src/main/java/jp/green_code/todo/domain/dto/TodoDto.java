package jp.green_code.todo.domain.dto;

/**
 * Todo 登録・更新フォーム
 */
public class TodoDto {
    // 新規の場合はnull
    private Long todoId;

    private String detail;

    private String deadline;

    private String todoStatus;
    public boolean isNew() {
        return todoId == null;
    }

    public String toLogString() {
        return String.format("TodoForm(todoId=%s,detail=%s,deadlineFrom=%s,todoStatus=%s)", todoId, detail, deadline, todoStatus);
    }

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(String todoStatus) {
        this.todoStatus = todoStatus;
    }
}
