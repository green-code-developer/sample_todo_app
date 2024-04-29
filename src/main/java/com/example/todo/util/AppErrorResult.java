package com.example.todo.util;

import io.micrometer.common.util.StringUtils;

public class AppErrorResult {

  // 画面に表示するためのエラーメッセージ、もしくはメッセージテンプレート
  private String errorMessage;

  // メッセージテンプレートに差し込む値
  private Object supplement;

  // 例外
  private Throwable throwable;

  // 成功の場合
  public AppErrorResult() {
    this(null, null, null);
  }

  // エラーの場合
  public AppErrorResult(String errorMessage) {
    this(errorMessage, null, null);
  }

  // エラーの場合で追加情報あり
  public AppErrorResult(String errorMessage, Object supplement) {
    this(errorMessage, supplement, null);
  }

  // エラーの場合で例外あり
  public AppErrorResult(String errorMessage, Throwable throwable) {
    this(errorMessage, null, throwable);
  }

  // エラーの場合で追加情報と例外あり
  public AppErrorResult(String errorMessage, Object supplement, Throwable throwable) {
    this.errorMessage = errorMessage;
    this.supplement = supplement;
    this.throwable = throwable;
  }

  public boolean isSuccess() {
    return StringUtils.isBlank(errorMessage);
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public Object getSupplement() {
    return supplement;
  }

  public void setSupplement(Object supplement) {
    this.supplement = supplement;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
  }
}
