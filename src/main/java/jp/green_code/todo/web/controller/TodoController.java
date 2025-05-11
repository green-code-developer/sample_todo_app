package jp.green_code.todo.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jp.green_code.todo.dto.common.AppNotification;
import jp.green_code.todo.dto.common.AppValidationResult;
import jp.green_code.todo.service.TodoService;
import jp.green_code.todo.web.form.TodoForm;
import jp.green_code.todo.web.form.TodoSearchForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static jp.green_code.todo.dto.common.AppMessage.propertyMessage;
import static jp.green_code.todo.dto.common.AppNotification.*;
import static jp.green_code.todo.util.ControllerUtil.getFlashAttribute;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequiredArgsConstructor
public class TodoController {

    public static final String LIST_PATH = "/list";
    public static final String KEY_PREV_CONDITION = "prevTodoListCondition";
    public static final String VALIDATION_ERROR_MESSAGE_KEY = "notification.validation-error";
    public static final String PREV_FORM = "prevForm";

    private final TodoService todoService;

    @RequestMapping(LIST_PATH)
    public ModelAndView list(HttpServletRequest request, RedirectAttributes redirectAttributes,
        @RequestParam(defaultValue = "false") boolean usePrevCondition, TodoSearchForm form) {
        // 前回の検索条件を使いたい場合
        if (usePrevCondition) {
            // セッションから前回の検索条件を取得してFlashAttribute へセット
            var conditionFromSession = request.getSession().getAttribute(KEY_PREV_CONDITION);
            redirectAttributes.addFlashAttribute(KEY_PREV_CONDITION, conditionFromSession);

            // Notification がFlashAttribute にあれば再度セット
            AppNotification notification = getFlashAttribute(request, NOTIFICATION);
            redirectAttributes.addFlashAttribute(NOTIFICATION, notification);

            // アドレスバーにusePrevCondition=true が残るのを避けるためリダイレクト
            return new ModelAndView("redirect:" + LIST_PATH);
        }

        // FlashAttribute に検索条件があればそちらを使う
        TodoSearchForm formFromFlashAttribute = getFlashAttribute(request, KEY_PREV_CONDITION);
        if (formFromFlashAttribute != null) {
            form = formFromFlashAttribute;
        }
        form = form == null ? new TodoSearchForm() : form;

        // 検索条件をsession に保存（前回の検索条件が必要な場合に備える）
        request.getSession().setAttribute(KEY_PREV_CONDITION, form);

        // 検索結果取得
        var pair = todoService.findByForm(form);

        var mav = new ModelAndView("todo/todo_list");
        mav.addObject("form", form);
        mav.addObject("app", pair.getLeft());
        mav.addObject("pageableList", pair.getRight());
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView newTodo(HttpServletRequest request) {
        return edit(request, new AppValidationResult<>(new TodoForm()));
    }

    @GetMapping("/edit/{todoId}")
    public ModelAndView editTodo(HttpServletRequest request, @PathVariable long todoId) {
        var todoEntity = todoService.findByTodoId(todoId);
        if (todoEntity.isEmpty()) {
            // 404
            throw new ResponseStatusException(NOT_FOUND);
        }
        var form = TodoService.entityToForm(todoEntity.get());
        return edit(request, new AppValidationResult<>(form));
    }

    ModelAndView edit(HttpServletRequest request, AppValidationResult<TodoForm> app) {
        // FlashAttribute に値があればそちらを使う
        AppValidationResult<TodoForm> appFromFlashAttribute = getFlashAttribute(request, PREV_FORM);
        if (appFromFlashAttribute != null) {
            app = appFromFlashAttribute;
        }

        var mav = new ModelAndView("todo/todo_edit");
        mav.addObject("app", app);
        mav.addObject("form", app.getForm());
        return mav;
    }

    @PostMapping("/upsert")
    public String newTodoPost(RedirectAttributes redirectAttributes, TodoForm form) {
        var result = todoService.save(form);

        if (!result.getLeft().isSuccess()) {
            // Notification でエラーを表示
            var errorNotification = errorNotification(propertyMessage(VALIDATION_ERROR_MESSAGE_KEY));
            redirectAttributes.addFlashAttribute(NOTIFICATION, errorNotification);

            // バリデーション結果とForm をFlashAttribute へ保存
            redirectAttributes.addFlashAttribute(PREV_FORM, result.getLeft());

            // 再度登録画面へ
            if (form.isNew()) {
                return "redirect:/new";
            } else {
                return "redirect:/edit/" + form.getTodoId();
            }
        }
        // 成功時は一覧に戻る
        // 成功メッセージを表示するためflash attribute にセット
        var messageCode = result.getRight() ? "notification.complete-post" : "notification.complete-update";
        var successNotification = successNotification(propertyMessage(messageCode));
        redirectAttributes.addFlashAttribute(NOTIFICATION, successNotification);
        return "redirect:" + LIST_PATH + "?usePrevCondition=true";
    }

    @GetMapping("/setting")
    public ModelAndView setting() {
        return new ModelAndView("todo/todo_setting");
    }
}
