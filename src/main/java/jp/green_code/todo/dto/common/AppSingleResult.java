package jp.green_code.todo.dto.common;

import lombok.Data;

@Data
public class AppSingleResult<T> {

  private T value;

  private AppErrorResult result;
}
