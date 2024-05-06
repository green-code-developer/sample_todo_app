package jp.green_code.todo.todo.util;

import io.micrometer.common.util.StringUtils;

public class AppErrorResult {

  // メッセージキー(messages.properties のキー)
  private String messageKey;

  // メッセージに差し込む値
  private Object[] params;

  // 成功の場合
  public AppErrorResult() {
    this(null);
  }

  public AppErrorResult(String messageKey) {
    this(messageKey, new Object[]{});
  }

  public AppErrorResult(String messageKey, Object... params) {
    this.messageKey = messageKey;
    this.params = params;
  }

  public boolean isSuccess() {
    return StringUtils.isBlank(messageKey);
  }

  public String getMessageKey() {
    return messageKey;
  }

  public void setMessageKey(String messageKey) {
    this.messageKey = messageKey;
  }

  public Object[] getParams() {
    return params;
  }

  public void setParams(Object... params) {
    this.params = params;
  }
}
