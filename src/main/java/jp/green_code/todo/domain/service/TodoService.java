package jp.green_code.todo.domain.service;

import jp.green_code.todo.domain.dto.TodoDto;
import jp.green_code.todo.domain.dto.TodoSearchDto;
import jp.green_code.todo.domain.entity.TodoEntity;
import jp.green_code.todo.domain.enums.TodoSearchSortEnum;
import jp.green_code.todo.domain.enums.TodoStatusEnum;
import jp.green_code.todo.domain.repository.TodoRepository;
import jp.green_code.shared.component.DateComponent;
import jp.green_code.shared.component.LogFormatter;
import jp.green_code.shared.dto.PageResult;
import jp.green_code.shared.dto.Validated;
import jp.green_code.shared.dto.Validated.Invalid;
import jp.green_code.shared.dto.Validated.Valid;
import jp.green_code.shared.dto.ValidationStepResult;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static jp.green_code.todo.domain.enums.TodoStatusEnum.NEW;
import static jp.green_code.shared.component.DateComponent.FORMAT_YMD_hyphen_loose;
import static jp.green_code.shared.component.DateComponent.parseYMD_hyphen_loose;
import static jp.green_code.shared.component.DateComponent.toYMD_hyphen;
import static jp.green_code.shared.dto.Validated.toValidated;
import static jp.green_code.shared.util.ThreadLocalUtil.methodName;
import static jp.green_code.shared.util.ValidationUtil.enumValidator;
import static jp.green_code.shared.util.ValidationUtil.localDateTextValidator;
import static jp.green_code.shared.util.ValidationUtil.min;
import static jp.green_code.shared.util.ValidationUtil.stopIfEmpty;
import static jp.green_code.shared.util.ValidationUtil.textMaxLength;
import static jp.green_code.shared.util.ValidationUtil.textMinLength;
import static jp.green_code.shared.util.ValidationUtil.validateAllowNull;
import static jp.green_code.shared.util.ValidationUtil.validateRequired;
import static org.apache.commons.lang3.EnumUtils.getEnumIgnoreCase;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@Transactional(rollbackFor = Exception.class)
public class TodoService {

    private final TodoRepository todoRepository;
    private final LogFormatter logFormatter;
    private final DateComponent dateComponent;
    private final Logger log = getLogger(this.getClass());

    public TodoService(TodoRepository todoRepository, LogFormatter logFormatter, DateComponent dateComponent) {
        this.todoRepository = todoRepository;
        this.logFormatter = logFormatter;
        this.dateComponent = dateComponent;
    }

    public Optional<TodoEntity> findByTodoId(long todoId) {
        log.info(logFormatter.start(methodName(), "todoId=%d".formatted(todoId)));
        var result = todoRepository.findByPk(todoId);
        log.info(logFormatter.end(methodName(), ""));
        return result;
    }

    // TodoForm の値をバリデーション後にDB 登録する
    public Validated<TodoEntity> save(TodoDto dto) {
        log.info(logFormatter.start(methodName(), dto.toLogString()));

        return switch (validateAndBuildEntity(dto)) {
            case Invalid<TodoEntity> e -> {
                log.info(logFormatter.validationError(methodName(), e));
                yield e;
            }
            case Valid<TodoEntity> e -> {
                var todoEntity = e.value();
                var isNew = todoEntity.getTodoId() == null;
                if (isNew) {
                    todoRepository.insert(todoEntity);
                } else {
                    // 更新者ID をセットする(ここではダミー値を使用)
                    long dummyAccountId = -1L;
                    todoEntity.setUpdatedBy(dummyAccountId);
                    todoRepository.update(todoEntity);
                }
                log.info(logFormatter.end(methodName(), "isNew=%s".formatted(isNew)));
                yield e;
            }
        };
    }

    Validated<TodoEntity> validateAndBuildEntity(TodoDto dto) {
        var entity = new TodoEntity();
        entity.setTodoId(dto.getTodoId());
        entity.setDetail(dto.getDetail());
        entity.setTodoStatus(NEW);

        var map = new HashMap<String, ValidationStepResult>();

        var todoIdResult = validateAllowNull(dto.getTodoId(), min(0L, true));
        map.put("todoId", todoIdResult);

        var detailValidators = List.of(textMinLength(1), textMaxLength(100));
        var detailResult = validateRequired(dto.getDetail(), detailValidators);
        map.put("detail", detailResult);

        var deadlineValidators = List.of(stopIfEmpty(), localDateTextValidator(FORMAT_YMD_hyphen_loose, ldt -> entity.setDeadline(dateComponent.localDateToOffsetDateTime(ldt))));
        var deadlineResult = validateAllowNull(dto.getDeadline(), deadlineValidators);
        map.put("deadline", deadlineResult);

        var statusValidators = List.of(stopIfEmpty(), enumValidator(TodoStatusEnum.class, entity::setTodoStatus));
        var statusResult = validateAllowNull(dto.getTodoStatus(), statusValidators);
        map.put("status", statusResult);

        return toValidated(map, entity);
    }

    public static TodoDto entityToDto(TodoEntity entity) {
        var result = new TodoDto();
        result.setTodoId(entity.getTodoId());
        result.setDetail(entity.getDetail());
        result.setTodoStatus(entity.getTodoStatus() + "");
        String deadline = toYMD_hyphen(entity.getDeadline());
        result.setDeadline(deadline);
        return result;
    }

    public Validated<PageResult<TodoEntity>> search(TodoSearchDto dto) {
        log.info(logFormatter.start(methodName(), dto.toLogString()));

        return switch (validateSearchDto(dto)) {
            case Invalid<Boolean> e -> {
                log.info(logFormatter.validationError(methodName(), e));
                yield new Invalid<>(e.errors());
            }
            case Valid<Boolean> e -> {
                // dto の値を検索可能な型に変換
                var sort = getEnumIgnoreCase(TodoSearchSortEnum.class, dto.getSort(), null);
                var status = getEnumIgnoreCase(TodoStatusEnum.class, dto.getStatus(), null);
                var deadlineFrom = parseYMD_hyphen_loose(dto.getDeadlineFrom()).map(dateComponent::toOffsetDateTime).orElse(null);
                var deadlineTo = parseYMD_hyphen_loose(dto.getDeadlineTo()).map(dateComponent::toOffsetDateTime).orElse(null);

                // データ取得
                var result = todoRepository.findByCondition(dto, sort, dto.getWord(), status, deadlineFrom, deadlineTo);

                // 戻り値作成
                log.info(logFormatter.end(methodName(), "count=%s".formatted(result.totalCount())));
                yield new Valid<>(result);
            }
        };
    }

    Validated<Boolean> validateSearchDto(TodoSearchDto dto) {
        var map = new HashMap<String, ValidationStepResult>();

        var wordResult = validateAllowNull(dto.getWord(), textMaxLength(100));
        map.put("word", wordResult);

        var statusResult = validateAllowNull(dto.getStatus(), List.of(stopIfEmpty(), enumValidator(TodoStatusEnum.class, null)));
        map.put("status", statusResult);

        var deadlineValidators = List.of(stopIfEmpty(), localDateTextValidator(FORMAT_YMD_hyphen_loose, null));
        var deadlineFromResult = validateAllowNull(dto.getDeadlineFrom(), deadlineValidators);
        map.put("deadlineFrom", deadlineFromResult);

        var deadlineToResult = validateAllowNull(dto.getDeadlineTo(), deadlineValidators);
        map.put("deadlineTo", deadlineToResult);

        var sortResult = validateAllowNull(dto.getSort(), List.of(stopIfEmpty(), enumValidator(TodoSearchSortEnum.class, null)));
        map.put("sort", sortResult);

        return toValidated(map, true);
    }
}
