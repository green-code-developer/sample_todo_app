package jp.green_code.todo.dto.common;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppErrorResult {

    private List<AppMessage> appMessageList = new ArrayList<>();

    public AppErrorResult(AppMessage appMessage) {
        appMessageList.add(appMessage);
    }

    public boolean isSuccess() {
        return appMessageList.isEmpty();
    }
}
