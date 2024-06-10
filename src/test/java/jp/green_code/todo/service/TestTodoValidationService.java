package jp.green_code.todo.service;

import jp.green_code.todo.web.form.TodoForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestTodoValidationService {

  // detail の長さ上限文字列
  public static String MAX_DETAIL_LENGTH_VALUE;
  static {
    var sb = new StringJoiner("");
    for (int i = 0; i < TodoValidationService.MAX_DETAIL_LENGTH; i++) {
      sb.add("あ");
    }
    MAX_DETAIL_LENGTH_VALUE = sb + "";
  }

  private final TodoValidationService todoValidationService;

  @Autowired
  public TestTodoValidationService(
      TodoValidationService todoValidationService
  ) {
    this.todoValidationService = todoValidationService;
  }

  @Test
  public void validateDetail() {
    assertionDetail(true, "1");
    assertionDetail(true, MAX_DETAIL_LENGTH_VALUE);
    assertionDetail(false, null);
    assertionDetail(false, "");
    assertionDetail(false, MAX_DETAIL_LENGTH_VALUE + "1");
  }

  private void assertionDetail(boolean expectToSuccess, String detail) {
    var result = todoValidationService.validateDetail(detail);
    Assertions.assertEquals(expectToSuccess, result.isSuccess());
    if (!expectToSuccess) {
      Assertions.assertEquals(TodoValidationService.DETAIL_ERROR_MESSAGE_KEY, result.getAppMessage().getCode());
    }
  }

  @Test
  public void validateDeadline() {
    assertDeadline(true, "1900-01-01");
    assertDeadline(true, "1900-1-1");
    assertDeadline(true, "2023-12-01");
    assertDeadline(true, "2023-12-1");
    assertDeadline(true, null);
    assertDeadline(true, "");
    assertDeadline(false, "a1900-1-1");
  }

  private void assertDeadline(boolean expectToSuccess, String deadline) {
    var result = todoValidationService.validateDeadline(deadline);
    Assertions.assertEquals(expectToSuccess, result.isSuccess());
    if (!expectToSuccess) {
      Assertions.assertEquals(TodoValidationService.DEADLINE_ERROR_MESSAGE_KEY, result.getAppMessage().getCode());
    }
  }

  @Test
  public void validate() {
    assertTodo(true, true, "ok", "1900-1-1");
    assertTodo(false, true, "", "1900-1-1");
    assertTodo(true, false, "ok", "1900-a-1");
    assertTodo(false, false, "", "1900-a-1");
  }

  private void assertTodo(
      boolean isDetailSuccess, boolean isDeadlineSuccess,
      String detail, String deadline) {
    var form = new TodoForm();
    form.setDetail(detail);
    form.setDeadline(deadline);

    var result = todoValidationService.validate(form);
    Assertions.assertEquals(isDetailSuccess && isDeadlineSuccess, result.isSuccess());
    Assertions.assertEquals(isDetailSuccess, result.getMap().get("detail").isSuccess());
    Assertions.assertEquals(isDeadlineSuccess, result.getMap().get("deadline").isSuccess());
  }
}
