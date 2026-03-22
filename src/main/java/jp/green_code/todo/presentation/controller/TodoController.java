package jp.green_code.todo.presentation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jp.green_code.todo.domain.dto.TodoDto;
import jp.green_code.todo.domain.dto.TodoSearchDto;
import jp.green_code.todo.domain.entity.TodoEntity;
import jp.green_code.todo.domain.service.TodoService;
import jp.green_code.shared.dto.MessageKey;
import jp.green_code.shared.dto.Notification;
import jp.green_code.shared.dto.PageResult;
import jp.green_code.shared.dto.Validated;
import jp.green_code.shared.dto.Validated.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static jp.green_code.shared.dto.Notification.NOTIFICATION;
import static jp.green_code.shared.dto.Notification.NotificationType.NOTIFICATION_ERROR;
import static jp.green_code.shared.dto.Notification.NotificationType.NOTIFICATION_SUCCESS;
import static jp.green_code.shared.util.ControllerUtil.getFlashAttribute;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class TodoController {

    public static final String LIST_PATH = "/list";
    public static final String PREV_CONDITION = "prevTodoListCondition";
    public static final String VALIDATION_ERROR_MESSAGE_KEY = "notification.validation-error";
    public static final String PREV_FORM = "prevForm";
    public static final String PREV_VALIDATED = "prevValidated";

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @RequestMapping(LIST_PATH)
    public ModelAndView list(HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam(defaultValue = "false") boolean usePrevCondition, TodoSearchDto dto) {
        // 前回の検索条件を使いたい場合
        if (usePrevCondition) {
            // セッションから前回の検索条件を取得してFlashAttribute へセット
            var conditionFromSession = request.getSession().getAttribute(PREV_CONDITION);
            redirectAttributes.addFlashAttribute(PREV_CONDITION, conditionFromSession);

            // Notification がFlashAttribute にあれば再度セット
            getFlashAttribute(request, NOTIFICATION, Notification.class).ifPresent(notification -> redirectAttributes.addFlashAttribute(NOTIFICATION, notification));

            // アドレスバーにusePrevCondition=true が残るのを避けるためリダイレクト
            return new ModelAndView("redirect:" + LIST_PATH);
        }

        // FlashAttribute に検索条件があればそちらを使う
        dto = getFlashAttribute(request, PREV_CONDITION, TodoSearchDto.class).orElse(dto);

        // 検索条件をsession に保存（前回の検索条件が必要な場合に備える）
        request.getSession().setAttribute(PREV_CONDITION, dto);

        // 検索結果取得
        var result = todoService.search(dto);

        var mav = new ModelAndView("todo/todo_list");
        mav.addObject("dto", dto);
        mav.addObject("result", result);
        List<TodoEntity> list = List.of();
        if (result instanceof Valid<PageResult<TodoEntity>> valid) {
            mav.addObject("page", valid.value());
            list = valid.value().list();
        }
        mav.addObject("list", list);
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView newTodo(HttpServletRequest request) {
        return edit(request, new TodoDto());
    }

    @GetMapping("/edit/{todoId}")
    public ModelAndView editTodo(HttpServletRequest request, @PathVariable long todoId) {
        var todoEntity = todoService.findByTodoId(todoId);
        if (todoEntity.isEmpty()) {
            // 404
            throw new ResponseStatusException(NOT_FOUND);
        }
        var dtoFromEntity = TodoService.entityToDto(todoEntity.get());
        return edit(request, dtoFromEntity);
    }

    ModelAndView edit(HttpServletRequest request, TodoDto dtoFromEntity) {
        // FlashAttribute に値があればそちらを使う
        TodoDto dto = getFlashAttribute(request, PREV_FORM, TodoDto.class).orElse(dtoFromEntity);
        var result = getValidationFlash(request, PREV_VALIDATED).orElse(new Valid<>(new TodoEntity()));

        var mav = new ModelAndView("todo/todo_edit");
        mav.addObject("result", result);
        mav.addObject("dto", dto);
        return mav;
    }

    // Generics を使っている場合はcast 時に警告が出るため短いメソッドで部分的に抑止する
    @SuppressWarnings("unchecked")
    public static Optional<Validated<TodoEntity>> getValidationFlash(HttpServletRequest request, String key) {
        return getFlashAttribute(request, key, Object.class).map(c -> (Validated<TodoEntity>) c);
    }

    @PostMapping("/upsert")
    public String newTodoPost(RedirectAttributes redirectAttributes, TodoDto dto) {
        return switch (todoService.save(dto)) {
            case Validated.Invalid<TodoEntity> e -> {
                // Notification でエラーを表示
                var errorNotification = new Notification(NOTIFICATION_ERROR, new MessageKey(VALIDATION_ERROR_MESSAGE_KEY));
                redirectAttributes.addFlashAttribute(NOTIFICATION, errorNotification);

                // バリデーション結果とForm をFlashAttribute へ保存
                redirectAttributes.addFlashAttribute(PREV_FORM, dto);
                redirectAttributes.addFlashAttribute(PREV_VALIDATED, e);

                // 再度登録画面へ
                if (dto.isNew()) {
                    yield "redirect:/new";
                } else {
                    yield "redirect:/edit/" + dto.getTodoId();
                }
            }
            case Valid<TodoEntity> ignore -> {
                // 成功時は一覧に戻る
                // 成功メッセージを表示するためflash attribute にセット
                var messageKey = dto.isNew() ? "notification.complete-post" : "notification.complete-update";
                var successNotification = new Notification(NOTIFICATION_SUCCESS, new MessageKey(messageKey));
                redirectAttributes.addFlashAttribute(NOTIFICATION, successNotification);
                yield "redirect:" + LIST_PATH + "?usePrevCondition=true";
            }
        };
    }
}
