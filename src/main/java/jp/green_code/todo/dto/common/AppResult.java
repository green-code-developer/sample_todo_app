package jp.green_code.todo.dto.common;

import lombok.Data;

@Data
public class AppResult<T> {

  private T value;

  private AppErrorResult result;
}
