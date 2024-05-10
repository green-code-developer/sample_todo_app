package jp.green_code.todo.todo.util;

public class AppErrorResult {

    private final AppMessage appMessage;

    // 成功の場合
    public AppErrorResult() {
        appMessage = null;
    }

    public AppErrorResult(AppMessage appMessage) {
        this.appMessage = appMessage;
    }

    public boolean isSuccess() {
        return appMessage == null;
    }

    public AppMessage getAppMessage() {
        return appMessage;
    }
}
