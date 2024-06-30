package jp.green_code.todo.service;

import static jp.green_code.todo.enums.TodoSearchSortEnum.UPDATE_DESC;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.Arrays;
import java.util.Optional;
import jp.green_code.todo.dto.common.AppPageableList;
import jp.green_code.todo.dto.common.AppValidationResult;
import jp.green_code.todo.entity.TodoEntity;
import jp.green_code.todo.enums.TodoSearchSortEnum;
import jp.green_code.todo.repository.TodoJpaRepository;
import jp.green_code.todo.util.DateUtil;
import jp.green_code.todo.util.JsonUtil;
import jp.green_code.todo.util.ValidationUtil;
import jp.green_code.todo.web.form.TodoForm;
import jp.green_code.todo.web.form.TodoSearchForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * やること
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoJpaRepository todoJpaRepository;
    private final ValidationUtil validationUtil;

    public static TodoEntity formToEntity(TodoForm form) {
        var result = new TodoEntity();
        copyProperties(form, result);
        var deadline = DateUtil.parseYMD_hyphen_loose(form.getDeadline())
                .map(DateUtil::localDateToOffsetDateTime).orElse(null);
        result.setDeadline(deadline);
        result.setTodoStatus(form.toStatusEnum().map(e -> e + "").orElse(null));
        return result;
    }

    // TodoForm の値をバリデーション後にDB 登録する
    public Pair<AppValidationResult<TodoForm>, Boolean> save(TodoForm form) {
        log.info("START upsert() form({})", JsonUtil.toJson(form));

        // バリデーションを行う
        var validationResult = validationUtil.validate(form);
        if (!validationResult.isSuccess()) {
            log.info("END save() validation error ({})", JsonUtil.toJson(validationResult.getErrorMap()));
            return Pair.of(validationResult, null);
        }
        // フォームに変換
        var todoEntity = formToEntity(form);
        // DB 登録
        boolean isNew = todoEntity.getTodoId() == null;
        todoEntity = todoJpaRepository.save(todoEntity);
        var storedEntity = findByTodoId(todoEntity.getTodoId()).get();
        var storedEntityAsForm = entityToForm(storedEntity);
        log.info("END " + this.getClass().getSimpleName() + ".upsert");
        return Pair.of(validationResult, isNew);
    }

    public static TodoForm entityToForm(TodoEntity entity) {
        var result = new TodoForm();
        copyProperties(entity, result);
        String deadline = DateUtil.toYMD_hyphen(entity.getDeadline());
        result.setDeadline(deadline);
        return result;
    }

    public Pair<AppValidationResult<TodoSearchForm>, AppPageableList<TodoEntity>> findByForm(TodoSearchForm form) {
        log.info("START findByForm() form({})", JsonUtil.toJson(form));

        // バリデーション
        var validationResult = validationUtil.validate(form);
        if (!validationResult.isSuccess()) {
            log.info("END save() validation error ({})", JsonUtil.toJson(validationResult.getErrorMap()));
            return Pair.of(validationResult, AppPageableList.empty());
        }

        // sortEnum を取得
        var sortEnum = Arrays.stream(TodoSearchSortEnum.values())
            .filter(e -> StringUtils.equals(e + "", form.getSort())).findFirst()
            .orElse(UPDATE_DESC);

        // ページリクエスト作成
        var pageRequest = form.toPageRequest(sortEnum.getSort());

        // データ取得
        var page = todoJpaRepository.findByForm(pageRequest, form.getWord(), form.getStatus(),
            form.getDeadlineFrom(), form.getDeadlineTo());

        // 戻り値作成
        var result = new AppPageableList<>(page.getContent(), page.getTotalElements(), form.getCurrentPage(),
            form.getPageSize());
        log.info("END findByForm() count({})", page.getTotalElements());
        return Pair.of(null, result);
    }

    public Optional<TodoEntity> findByTodoId(long todoId) {
        log.info("START " + this.getClass().getSimpleName() + ".findByTodoId todoId({})", todoId);
        var result = todoJpaRepository.findById(todoId);
        log.info("END " + this.getClass().getSimpleName() + ".findByTodoId todoId({})", todoId);
        return result;
    }
}
