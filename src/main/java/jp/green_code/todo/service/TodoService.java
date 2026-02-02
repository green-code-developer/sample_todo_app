package jp.green_code.todo.service;

import jp.green_code.todo.dto.common.AppPageableList;
import jp.green_code.todo.dto.common.AppValidationResult;
import jp.green_code.todo.entity.TodoEntity;
import jp.green_code.todo.enums.TodoSearchSortEnum;
import jp.green_code.todo.enums.TodoStatusEnum;
import jp.green_code.todo.repository.TodoRepository;
import jp.green_code.todo.util.DateUtil;
import jp.green_code.todo.util.ValidationUtil;
import jp.green_code.todo.web.form.TodoForm;
import jp.green_code.todo.web.form.TodoSearchForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static jp.green_code.todo.enums.TodoStatusEnum.NEW;
import static jp.green_code.todo.util.DateUtil.parseYMD_hyphen_loose;
import static jp.green_code.todo.util.DateUtil.toYMD_hyphen;
import static jp.green_code.todo.util.JsonUtil.toJson;
import static jp.green_code.todo.util.ThreadLocalUtil.methodName;
import static org.apache.commons.lang3.EnumUtils.getEnumIgnoreCase;

/**
 * やること
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;
    private final ValidationUtil validationUtil;


    public Optional<TodoEntity> findByTodoId(long todoId) {
        log.info("START {} todoId({})", methodName(), todoId);
        var result = todoRepository.findByPk(todoId);
        log.info("END {} todoId({})", methodName(), todoId);
        return result;
    }

    // TodoForm の値をバリデーション後にDB 登録する
    public Pair<AppValidationResult<TodoForm>, Boolean> save(TodoForm form) {
        log.info("START {} form({})", methodName(), toJson(form));

        var validationResult = validationUtil.validate(form);
        if (!validationResult.isSuccess()) {
            var logJson = toJson(validationResult.getErrorMap());
            log.info("END {} validation error ({})", methodName(), logJson);
            return Pair.of(validationResult, null);
        }
        // フォームに変換
        var todoEntity = formToEntity(form);
        // DB 登録
        var isNew = todoEntity.getTodoId() == null;
        todoRepository.upsert(todoEntity);
        log.info("END {}", methodName());
        return Pair.of(validationResult, isNew);
    }

    public static TodoEntity formToEntity(TodoForm form) {
        var result = new TodoEntity();
        result.setTodoId(form.getTodoId());
        result.setDetail(form.getDetail());
        var deadline = parseYMD_hyphen_loose(form.getDeadline())
                .map(DateUtil::localDateToOffsetDateTime).orElse(null);
        result.setDeadline(deadline);
        var status = getEnumIgnoreCase(TodoStatusEnum.class, form.getTodoStatus(), NEW);
        result.setTodoStatus(status);
        return result;
    }

    public static TodoForm entityToForm(TodoEntity entity) {
        var result = new TodoForm();
        result.setTodoId(entity.getTodoId());
        result.setDetail(entity.getDetail());
        result.setTodoStatus(entity.getTodoStatus() + "");
        String deadline = toYMD_hyphen(entity.getDeadline());
        result.setDeadline(deadline);
        return result;
    }

    public Pair<AppValidationResult<TodoSearchForm>, AppPageableList<TodoEntity>> findByForm(TodoSearchForm form) {
        log.info("START {} form({})", methodName(), toJson(form));

        // バリデーション
        var validationResult = validationUtil.validate(form);
        if (!validationResult.isSuccess()) {
            var logJson = toJson(validationResult.getErrorMap());
            log.info("END {} validation error ({})", methodName(), logJson);
            return Pair.of(validationResult, AppPageableList.empty());
        }

        // form の値を検索可能な型に変換
        var sort = getEnumIgnoreCase(TodoSearchSortEnum.class, form.getSort(), null);
        var status = getEnumIgnoreCase(TodoStatusEnum.class, form.getStatus(), null);
        var deadlineFrom = parseYMD_hyphen_loose(form.getDeadlineFrom()).map(DateUtil::toOffsetDateTime).orElse(null);
        var deadlineTo = parseYMD_hyphen_loose(form.getDeadlineTo()).map(DateUtil::toOffsetDateTime).orElse(null);

        // データ取得
        var result = todoRepository.findByCondition(form, sort, form.getWord(), status, deadlineFrom, deadlineTo);

        // 戻り値作成
        log.info("END {} count({})", methodName(), result.getCount());
        return Pair.of(null, result);
    }
}
