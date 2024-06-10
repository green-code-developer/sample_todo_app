package jp.green_code.todo.service;

import jp.green_code.todo.repository.TodoCrudRepository;
import jp.green_code.todo.repository.TodoJdbcRepository;
import jp.green_code.todo.entity.TodoEntity;
import jp.green_code.todo.enums.TodoStatusEnum;
import jp.green_code.todo.util.AppMultiResult;
import jp.green_code.todo.util.AppPageableList;
import jp.green_code.todo.util.DateUtil;
import jp.green_code.todo.web.form.TodoForm;
import jp.green_code.todo.web.form.TodoSearchForm;
import jp.green_code.todo.util.JsonUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static jp.green_code.todo.util.JsonUtil.toJson;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Transactional(rollbackFor = Exception.class)
public class TodoService {

    private final Logger logger = getLogger(this.getClass());
    private final TodoValidationService todoValidationService;
    private final TodoCrudRepository todoCrudRepository;
    private final TodoJdbcRepository todoJdbcRepository;

    public TodoService(
            TodoValidationService todoValidationService,
            TodoCrudRepository todoCrudRepository,
            TodoJdbcRepository todoJdbcRepository) {
        this.todoValidationService = todoValidationService;
        this.todoCrudRepository = todoCrudRepository;
        this.todoJdbcRepository = todoJdbcRepository;
    }

    public static TodoEntity formToEntity(TodoForm form) {
        var result = new TodoEntity();
        copyProperties(form, result);
        var deadline = DateUtil.parseYMD_hyphen_loose(form.getDeadline())
                .map(DateUtil::localDateToOffsetDateTime).orElse(null);
        result.setDeadline(deadline);
        return result;
    }

    // TodoForm の値をバリデーション後にDB 登録する
    public Pair<AppMultiResult<TodoForm>, Boolean> upsert(TodoForm form, long updatedBy) {
        logger.info("S " + this.getClass().getSimpleName() + ".upsert form({}), updatedBy({})",
                JsonUtil.toJson(form), updatedBy);

        // バリデーションを行う
        var result = todoValidationService.validate(form);
        // エラーなら終了
        if (!result.isSuccess()) {
            logger.info("E " + this.getClass().getSimpleName() + ".upsert validation error ({})",
                    JsonUtil.toJson(result.getErrorMap()));
            return Pair.of(result, null);
        }
        // フォームに変換
        var todoEntity = formToEntity(form);
        // DB 登録
        long todoId;
        boolean isNew;
        if (todoEntity.getTodoId() == null) {
            todoEntity.setTodoStatus(TodoStatusEnum.NEW + "");
            todoId = todoCrudRepository.insert(todoEntity, updatedBy);
            isNew = true;
        } else {
            todoId = todoEntity.getTodoId();
            todoCrudRepository.update(todoEntity, updatedBy);
            isNew = false;
        }
        //noinspection OptionalGetWithoutIsPresent
        var storedEntity = todoCrudRepository.findById(todoId).get();
        var storedEntityAsForm = entityToForm(storedEntity);
        logger.info("E " + this.getClass().getSimpleName() + ".upsert");
        return Pair.of(new AppMultiResult<>(storedEntityAsForm), isNew);
    }

    public static TodoForm entityToForm(TodoEntity entity) {
        var result = new TodoForm();
        copyProperties(entity, result);
        String deadline = DateUtil.toYMD_hyphen(entity.getDeadline());
        result.setDeadline(deadline);
        return result;
    }

    public AppPageableList<TodoEntity> findByForm(TodoSearchForm form, long updatedBy) {
        logger.info("S " + this.getClass().getSimpleName() + ".findByForm form({}), updatedBy({})",
                JsonUtil.toJson(form), updatedBy);
        var list = todoJdbcRepository.findByForm(form);
        var count = todoJdbcRepository.countByForm(form);
        var result = new AppPageableList<>(list, count, form.getCurrentPage(), form.getPageSize());
        logger.info("E " + this.getClass().getSimpleName() + ".findByForm count({})", count);
        return result;
    }

    public Optional<TodoEntity> findByTodoId(long todoId) {
        return todoCrudRepository.findById(todoId);
    }
}
