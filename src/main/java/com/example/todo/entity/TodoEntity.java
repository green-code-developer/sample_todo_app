package com.example.todo.entity;

import static com.example.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;
import static com.example.todo.util.DateUtil.BLANK_LOCAL_TIME;

import java.time.LocalDateTime;

public class TodoEntity extends BaseEntity {

    private Long todoId = SYSTEM_ACCOUNT_ID;
    private String todoStatus = "";
    private String detail = "";
    private LocalDateTime deadline = BLANK_LOCAL_TIME;

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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
