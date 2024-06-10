package jp.green_code.todo.entity;

import static jp.green_code.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;

import java.time.OffsetDateTime;
import jp.green_code.todo.util.DateUtil;

public class TodoEntity extends BaseEntity {

    private Long todoId = SYSTEM_ACCOUNT_ID;
    private String todoStatus = "";
    private String detail = "";
    private OffsetDateTime deadline = DateUtil.BLANK_OFFSET_TIME;

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public String getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(String todoStatus) {
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
}
