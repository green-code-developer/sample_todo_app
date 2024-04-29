package com.example.todo.service;

import static com.example.todo.util.DateUtil.dateTimeToDateString;
import static com.example.todo.util.DateUtil.toLocalDate;

import com.example.todo.entity.TodoEntity;
import com.example.todo.enums.TodoStatusEnum;
import com.example.todo.repository.TodoCrudRepository;
import com.example.todo.repository.TodoJdbcRepository;
import com.example.todo.util.AppMultiResult;
import com.example.todo.util.DateUtil;
import com.example.todo.util.ListWithCount;
import com.example.todo.web.form.TodoForm;
import com.example.todo.web.form.TodoSearchForm;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    public AppMultiResult<TodoEntity> upsert(TodoForm form, long updatedBy) {
        // バリデーションを行う
        AppMultiResult<TodoEntity> result = todoValidationService.validate(form);
        // エラーなら終了
        if (!result.isSuccess()) {
            return result;
        }
        // フォームに変換
        TodoEntity todoEntity = formToEntity(form);
        // DB 登録
        long todoId;
        if (todoEntity.getTodoId() == null) {
            todoEntity.setTodoStatus(TodoStatusEnum.NEW + "");
            todoId = todoCrudRepository.insert(todoEntity, updatedBy);
        } else {
            todoId = todoEntity.getTodoId();
            todoCrudRepository.update(todoEntity, updatedBy);
        }
        TodoEntity storedEntity = todoCrudRepository.findById(todoId).get();
        return new AppMultiResult(storedEntity);
    }

    public TodoEntity formToEntity(TodoForm form) {
        TodoEntity result = new TodoEntity();
        result.setTodoId(form.getTodoId());
        result.setDetail(form.getDetail());
        result.setTodoStatus(form.getTodoStatus());
        LocalDate deadlineAsLocalDate = toLocalDate(form.getDeadline());
        LocalDateTime deadLineAsLocalDateTime = deadlineAsLocalDate == null ? null : deadlineAsLocalDate.atStartOfDay();
        result.setDeadline(deadLineAsLocalDateTime);
        return result;
    }

    public TodoForm entityToForm(TodoEntity entity) {
        TodoForm result = new TodoForm();
        result.setTodoId(entity.getTodoId());
        result.setDetail(entity.getDetail());
        result.setTodoStatus(entity.getTodoStatus());
        String deadline = dateTimeToDateString(entity.getDeadline());
        result.setDeadline(deadline);
        return result;
    }

    public ListWithCount<TodoEntity> findTodo(TodoSearchForm form) {
        List<TodoEntity> list = todoJdbcRepository.find(form);
        long count = todoJdbcRepository.findCount(form);
        ListWithCount result = new ListWithCount<>();
        result.setList(list);
        result.setCount(count);
        return result;
    }

    public Optional<TodoEntity> findByTodoId(long todoId) {
        return todoCrudRepository.findById(todoId);
    }
}
