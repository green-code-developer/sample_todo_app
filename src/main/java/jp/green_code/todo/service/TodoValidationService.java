package jp.green_code.todo.service;

import jp.green_code.todo.util.AppErrorResult;
import jp.green_code.todo.util.AppMultiResult;
import jp.green_code.todo.util.AppSuccessResult;
import jp.green_code.todo.web.form.TodoForm;
import org.springframework.stereotype.Service;

import static jp.green_code.todo.util.AppMessage.toMessage;
import static jp.green_code.todo.util.DateUtil.parseYMD_hyphen_loose;
import static org.apache.commons.lang3.StringUtils.*;

@Service
public class TodoValidationService {

    public static final int MAX_DETAIL_LENGTH = 100;
    public static final String DETAIL_ERROR_MESSAGE_KEY = "validation.todo";
    public static final String DEADLINE_ERROR_MESSAGE_KEY = "validation.deadline";

    /*
     * TodoForm のバリデーションを行う
     * 複数のカラムを一度にチェックする
     */
    public AppMultiResult<TodoForm> validate(TodoForm form) {
        var result = new AppMultiResult<>(form);
        var detailError = validateDetail(form.getDetail());
        result.getMap().put("detail", detailError);
        var deadlineError = validateDeadline(form.getDeadline());
        result.getMap().put("deadline", deadlineError);
        return result;
    }

    // detail のバリデーション
    // AppSuccessResult か AppErrorResult のどちらかを必ず返す
    public AppErrorResult validateDetail(String detail) {
        // detail はtrim して1文字以上100文字以下
        detail = trim(detail);
        if (isBlank(detail)) {
            return new AppErrorResult(toMessage(DETAIL_ERROR_MESSAGE_KEY));
        }
        if (MAX_DETAIL_LENGTH < length(detail)) {
            return new AppErrorResult(toMessage(DETAIL_ERROR_MESSAGE_KEY));
        }
        return new AppSuccessResult();
    }

    // deadline のバリデーション
    // AppSuccessResult か AppErrorResult のどちらかを必ず返す
    public AppErrorResult validateDeadline(String deadline) {
        // 任意なので空白なら成功扱い
        if (isBlank(deadline)) {
            return new AppSuccessResult();
        }

        var localDate = parseYMD_hyphen_loose(deadline);
        return localDate.isEmpty() ? new AppErrorResult(toMessage(DEADLINE_ERROR_MESSAGE_KEY)) : new AppSuccessResult();
    }
}
