package jp.green_code.todo.web.form;

import static jp.green_code.todo.util.DateUtil.parseYMD_hyphen_loose;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jp.green_code.todo.enums.TodoStatusEnum;
import lombok.Data;

/**
 * Todo 登録・更新フォーム
 */
@Data
public class TodoForm {

  // 新規の場合はnull
  private Long todoId;

  @NotNull
  @Size(min = 1, max = 100)
  private String detail;

  private String deadline;

  private String todoStatus;

  @AssertTrue(message = "{validation.date-format}")
  public boolean isAssertionOfDeadline() {
    return isEmpty(deadline) || parseYMD_hyphen_loose(deadline).isPresent();
  }

  @AssertTrue(message = "{validation.invalid-choice}")
  public boolean isAssertionOfTodoStatus() {
    return isEmpty(todoStatus) || TodoStatusEnum.optionalValueOf(todoStatus).isPresent();
  }

  public boolean isNew() {
    return todoId == null;
  }
}
