package com.example.todo.service;

import static com.example.todo.service.TodoValidationService.MAX_DETAIL_LENGTH;
import static com.example.todo.service.TodoValidationService.MESSAGE_DEADLINE_ERROR;
import static com.example.todo.service.TodoValidationService.MESSAGE_DETAIL_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.todo.entity.TodoEntity;
import com.example.todo.util.AppErrorResult;
import com.example.todo.util.AppMultiResult;
import com.example.todo.web.form.TodoForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTodoValidationService {

  // detail の長さ上限文字列
  public static String MAX_DETAIL_LENGTH_VALUE;
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < MAX_DETAIL_LENGTH; i++) {
      sb.append("あ");
    }
    MAX_DETAIL_LENGTH_VALUE = sb.toString();
  }

  private TodoValidationService todoValidationService;

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
    AppErrorResult error = todoValidationService.validateDetail(detail);
    assertEquals(expectToSuccess, error.isSuccess());
    if (!expectToSuccess) {
      assertEquals(MESSAGE_DETAIL_ERROR, error.getErrorMessage());
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
    AppErrorResult error = todoValidationService.validateDeadline(deadline);
    assertEquals(expectToSuccess, error.isSuccess());
    if (!expectToSuccess) {
      assertEquals(MESSAGE_DEADLINE_ERROR, error.getErrorMessage());
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
    TodoForm form = new TodoForm();
    form.setDetail(detail);
    form.setDeadline(deadline);

    AppMultiResult<TodoEntity> result = todoValidationService.validate(form);
    assertEquals(isDetailSuccess && isDeadlineSuccess, result.isSuccess());
    assertEquals(isDetailSuccess, result.getMap().get("detail").isSuccess());
    assertEquals(isDeadlineSuccess, result.getMap().get("deadline").isSuccess());
  }
}
