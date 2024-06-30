package jp.green_code.todo.dto.common;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * バリデーションエラー結果
 */
@NoArgsConstructor
@Data
public class AppValidationResult<T> {

  private T form;
  private Map<String, AppErrorResult> map = new HashMap<>();

  // フォームの値も保持する
  public AppValidationResult(T form) {
    this.form = form;
  }

  // 全て成功しているか
  public boolean isSuccess() {
    return map.values().stream().allMatch(AppErrorResult::isSuccess);
  }

  public Map<String, AppErrorResult> getErrorMap() {
    return map.keySet().stream().filter(k -> !map.get(k).isSuccess())
            .collect(Collectors.toMap(e -> e, e -> map.get(e)));
  }
}
