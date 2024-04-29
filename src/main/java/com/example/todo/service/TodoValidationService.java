package com.example.todo.service;

import static com.example.todo.util.DateUtil.toLocalDate;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.length;
import static org.apache.commons.lang3.StringUtils.trim;

import com.example.todo.entity.TodoEntity;
import com.example.todo.util.AppErrorResult;
import com.example.todo.util.AppMultiResult;
import com.example.todo.util.AppSuccessResult;
import com.example.todo.web.form.TodoForm;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class TodoValidationService {

    public static final int MAX_DETAIL_LENGTH = 100;
    public static final String MESSAGE_DETAIL_ERROR = "やることは1文字以上100文字以下で記載してください";
    public static final String MESSAGE_DEADLINE_ERROR = "完了予定日はyyyy/MM/dd の形式で記載してください";

    /*
     * TodoForm のバリデーションを行う
     * 複数のカラムを一度にチェックする
     */
    public AppMultiResult<TodoEntity> validate(TodoForm form) {
        AppMultiResult<TodoEntity> result = new AppMultiResult<>();
        AppErrorResult detailError = validateDetail(form.getDetail());
        result.getMap().put("detail", detailError);
        AppErrorResult deadlineError = validateDeadline(form.getDeadline());
        result.getMap().put("deadline", deadlineError);
        return result;
    }

    // detail のバリデーション
    // AppSuccessResult か AppErrorResult のどちらかを必ず返す
    public AppErrorResult validateDetail(String detail) {
        // detail はtrim して1文字以上100文字以下
        detail = trim(detail);
        if (isBlank(detail)) {
            return new AppErrorResult(MESSAGE_DETAIL_ERROR);
        }
        if (MAX_DETAIL_LENGTH < length(detail)) {
            return new AppErrorResult(MESSAGE_DETAIL_ERROR);
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

        LocalDate localDate = toLocalDate(deadline);
        return localDate == null ? new AppErrorResult(MESSAGE_DEADLINE_ERROR) : new AppSuccessResult();
    }
}
