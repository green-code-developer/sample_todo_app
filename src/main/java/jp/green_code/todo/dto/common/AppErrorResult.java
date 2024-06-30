package jp.green_code.todo.dto.common;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class AppErrorResult {

    private List<AppMessage> appMessageList = new ArrayList<>();

    // 成功の場合
    public AppErrorResult() {
    }

    public AppErrorResult(AppMessage appMessage) {
        appMessageList.add(appMessage);
    }

    public boolean isSuccess() {
        return appMessageList.isEmpty();
    }
}
