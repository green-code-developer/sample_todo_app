package jp.green_code.todo.todo.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jp.green_code.todo.todo.service.TodoService;
import jp.green_code.todo.todo.util.AppMultiResult;
import jp.green_code.todo.todo.web.form.TodoForm;
import jp.green_code.todo.todo.web.form.TodoSearchForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Map;

import static jp.green_code.todo.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;
import static jp.green_code.todo.todo.util.AppMessage.toMessage;
import static jp.green_code.todo.todo.util.AppNotification.*;
import static jp.green_code.todo.todo.web.controller.TodoController.TODO_PATH_PREFIX;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping(TODO_PATH_PREFIX)
public class TodoController {

    public static final String TODO_PATH_PREFIX = "/todo";

    public static final String KEY_PREV_CONDITION = "prevTodoListCondition";
    public static final String VALIDATION_ERROR_MESSAGE_KEY = "notification.validation-error";
    public static final String PREV_FORM = "prevForm";

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    public static <T> T getFlashAttribute(HttpServletRequest request, String key) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            //noinspection unchecked
            return (T) inputFlashMap.get(key);
        }
        return null;
    }

    @RequestMapping({"/", ""})
    public ModelAndView list(HttpServletRequest request, RedirectAttributes redirectAttributes,
                             @RequestParam(defaultValue = "false") boolean usePrevCondition, TodoSearchForm form) {
        // 前回の検索条件を使いたい場合
        if (usePrevCondition) {
            // セッションから前回の検索条件を取得してFlashAttribute へセット
            var conditionFromSession = request.getSession().getAttribute(KEY_PREV_CONDITION);
            redirectAttributes.addFlashAttribute(KEY_PREV_CONDITION, conditionFromSession);

            // Notification がFlashAttribute にあれば再度セット
            var notification = getFlashAttribute(request, NOTIFICATION);
            redirectAttributes.addFlashAttribute(NOTIFICATION, notification);

            // アドレスバーにusePrevCondition=true が残るのを避けるためリダイレクト
            return new ModelAndView("redirect:/todo/");
        }

        // FlashAttribute に検索条件があればそちらを使う
        var formFromFlashAttribute = getFlashAttribute(request, KEY_PREV_CONDITION);
        if (formFromFlashAttribute != null) {
            form = (TodoSearchForm) formFromFlashAttribute;
        }
        form = form == null ? new TodoSearchForm() : form;

        // 検索条件をsession に保存（前回の検索条件が必要な場合に備える）
        request.getSession().setAttribute(KEY_PREV_CONDITION, form);

        // 検索結果取得
        var pageableList = todoService.findByForm(form, SYSTEM_ACCOUNT_ID);

        var mav = new ModelAndView("todo/todo_list");
        mav.addObject("form", form);
        mav.addObject("pageableList", pageableList);
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView newTodo(HttpServletRequest request) {
        return edit(request, new AppMultiResult<>(new TodoForm()));
    }

    @GetMapping("/edit/{todoId}")
    public ModelAndView editTodo(HttpServletRequest request, @PathVariable long todoId) {
        var todoEntity = todoService.findByTodoId(todoId);
        if (todoEntity.isEmpty()) {
            // 404
            ModelAndView mav = new ModelAndView("/error/error");
            mav.setStatus(NOT_FOUND);
            return mav;
        }
        var form = TodoService.entityToForm(todoEntity.get());
        return edit(request, new AppMultiResult<>(form));
    }

    ModelAndView edit(HttpServletRequest request, AppMultiResult<TodoForm> app) {
        // FlashAttribute に値があればそちらを使う
        var appFromFlashAttribute = getFlashAttribute(request, PREV_FORM);
        if (appFromFlashAttribute != null) {
            //noinspection unchecked
            app = (AppMultiResult<TodoForm>) appFromFlashAttribute;
        }

        var mav = new ModelAndView("todo/todo_edit");
        mav.addObject("app", app);
        // app.value と同じだが、th:object を使うためトップレベルに必要
        mav.addObject("form", app.getValue());
        return mav;
    }

    @PostMapping("/upsert")
    public String newTodoPost(RedirectAttributes redirectAttributes, TodoForm form) {
        // 実行ユーザID
        // ここでは固定値だが実際にはセッションやJWT からユーザIDを算出
        var result = todoService.upsert(form, SYSTEM_ACCOUNT_ID);

        if (!result.getLeft().isSuccess()) {
            // Notification でエラーを表示
            redirectAttributes.addFlashAttribute(NOTIFICATION,
                    errorNotification(toMessage(VALIDATION_ERROR_MESSAGE_KEY)));

            // バリデーション結果とForm をFlashAttribute へ保存
            redirectAttributes.addFlashAttribute(PREV_FORM, result.getLeft());

            // 再度登録画面へ
            if (form.isNew()) {
                return "redirect:" + TODO_PATH_PREFIX + "/new";
            } else {
                return "redirect:" + TODO_PATH_PREFIX + "/edit/" + form.getTodoId();
            }
        }
        // 成功時は一覧に戻る
        // 成功メッセージを表示するためflash attribute にセット
        var messageCode = result.getRight() ? "notification.complete-post" : "notification.complete-update";
        redirectAttributes.addFlashAttribute(NOTIFICATION, successNotification(toMessage(messageCode)));
        return "redirect:/todo/?usePrevCondition=true";
    }

    @GetMapping("/setting")
    public ModelAndView setting() {
        return new ModelAndView("todo/todo_setting");
    }
}
