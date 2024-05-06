package jp.green_code.todo.todo.service;

import static org.springframework.beans.BeanUtils.copyProperties;

import jp.green_code.todo.todo.entity.TodoEntity;
import jp.green_code.todo.todo.enums.TodoStatusEnum;
import jp.green_code.todo.todo.repository.TodoCrudRepository;
import jp.green_code.todo.todo.repository.TodoJdbcRepository;
import jp.green_code.todo.todo.util.AppMultiResult;
import jp.green_code.todo.todo.util.DateUtil;
import jp.green_code.todo.todo.util.ListWithCount;
import jp.green_code.todo.todo.web.form.TodoForm;
import jp.green_code.todo.todo.web.form.TodoSearchForm;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TodoService {

    private final TodoValidationService todoValidationService;
    private final TodoCrudRepository todoCrudRepository;
    private final TodoJdbcRepository todoJdbcRepository;

    public TodoService(
        TodoValidationService todoValidationService,
        TodoCrudRepository todoCrudRepository,
        TodoJdbcRepository todoJdbcRepository
    ) {
        this.todoValidationService = todoValidationService;
        this.todoCrudRepository = todoCrudRepository;
        this.todoJdbcRepository = todoJdbcRepository;
    }

    // TodoForm の値をバリデーション後にDB 登録する
    public Pair<AppMultiResult<TodoForm>, Boolean> upsert(TodoForm form, long updatedBy) {
        // バリデーションを行う
        var result = todoValidationService.validate(form);
        // エラーなら終了
        if (!result.isSuccess()) {
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
        // get できないはずがないのでチェック不要
        var storedEntity = todoCrudRepository.findById(todoId).get();
        var storedEntityAsForm = entityToForm(storedEntity);
        return Pair.of(new AppMultiResult<>(storedEntityAsForm), isNew);
    }

    public static TodoEntity formToEntity(TodoForm form) {
        var result = new TodoEntity();
        copyProperties(form, result);
        var deadline = DateUtil.parseYMD_hyphen_loose(form.getDeadline())
            .map(DateUtil::localDateToOffsetDateTime).orElse(null);
        result.setDeadline(deadline);
        return result;
    }

    public static TodoForm entityToForm(TodoEntity entity) {
        var result = new TodoForm();
        copyProperties(entity, result);
        String deadline = DateUtil.toYMD_hyphen(entity.getDeadline());
        result.setDeadline(deadline);
        return result;
    }

    public ListWithCount<TodoEntity> findByForm(TodoSearchForm form) {
        var list = todoJdbcRepository.findByForm(form);
        var count = todoJdbcRepository.countByForm(form);
        var result = new ListWithCount<TodoEntity>();
        result.setList(list);
        result.setCount(count);
        return result;
    }

    public Optional<TodoEntity> findByTodoId(long todoId) {
        return todoCrudRepository.findById(todoId);
    }
}
