package jp.green_code.todo.todo.util;

import java.util.HashMap;
import java.util.Map;

// 複数のエラーが発生し得る場合
public class AppMultiResult<T> {

  private T value;
  private Map<String, AppErrorResult> map = new HashMap<>();

  public AppMultiResult() {
  }

  // 成功時
  public AppMultiResult(T value) {
    setValue(value);
  }

  // 全て成功しているか
  public boolean isSuccess() {
    return map.values().stream().allMatch(AppErrorResult::isSuccess);
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public Map<String, AppErrorResult> getMap() {
    return map;
  }

  public void setMap(Map<String, AppErrorResult> map) {
    this.map = map;
  }
}
