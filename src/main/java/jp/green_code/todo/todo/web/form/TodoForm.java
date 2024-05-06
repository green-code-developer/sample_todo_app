package jp.green_code.todo.todo.web.form;

/**
 * Todo 登録・更新フォーム
 */
public class TodoForm {

  // 新規の場合はnull
  private Long todoId;

  // ステータスの更新は専用の処理で行うためここでは持たない

  private String detail;

  private String deadline;

  private String todoStatus;

  public boolean isNew() {
    return todoId == null;
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
