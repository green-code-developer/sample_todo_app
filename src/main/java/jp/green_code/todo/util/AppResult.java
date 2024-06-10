package jp.green_code.todo.util;

public class AppResult<T> {

  private T value;

  private AppErrorResult result;

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public AppErrorResult getResult() {
    return result;
  }

  public void setResult(AppErrorResult result) {
    this.result = result;
  }
}
