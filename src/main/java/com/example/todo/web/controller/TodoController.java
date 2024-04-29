package com.example.todo.web.controller;

import static com.example.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;
import static com.example.todo.web.controller.TodoController.TODO_PATH_PREFIX;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.example.todo.entity.TodoEntity;
import com.example.todo.service.TodoService;
import com.example.todo.util.AppMultiResult;
import com.example.todo.util.AppPagination;
import com.example.todo.web.form.TodoForm;
import com.example.todo.web.form.TodoSearchForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
@RequestMapping(TODO_PATH_PREFIX)
public class TodoController {

    public static final String FLASH_ATTRIBUTE_UPDATE_TODO = "updateTodo";
    public static final String FLASH_ATTRIBUTE_PREV_CONDITION = "prevCondition";
    public static final String SESSION_KEY_PREV_CONDITION = "prevCondition";

    private final TodoService todoService;

    @Autowired
    TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    public static final String TODO_PATH_PREFIX = "/todo";

    @GetMapping("")
    public String index() {
        return "redirect:" + TODO_PATH_PREFIX + "/";
    }

    @GetMapping("/")
    public ModelAndView indexSlash(
        HttpServletRequest request,
        RedirectAttributes redirectAttributes,
        HttpSession session,
        @RequestParam(defaultValue = "false") boolean usePrevCondition,
        @RequestParam(defaultValue = "") String word,
        @RequestParam(defaultValue = "") String status,
        @RequestParam(defaultValue = "") String deadlineFrom,
        @RequestParam(defaultValue = "") String deadlineTo,
        @RequestParam(defaultValue = "1") int currentPage,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(defaultValue = "") String sort
    ) {
        if (usePrevCondition) {
            // 前回の検索条件をセッションから取得しFlashAttribute へセット
            // 登録完了後に検索画面へ戻る際に使用する
            var form = (TodoSearchForm) session.getAttribute(SESSION_KEY_PREV_CONDITION);
            redirectAttributes.addFlashAttribute(FLASH_ATTRIBUTE_PREV_CONDITION, form);

            // 更新後のflashAttribute も保存しておく
            var newTodoResult = getFlashAttribute(request, FLASH_ATTRIBUTE_UPDATE_TODO);
            redirectAttributes.addFlashAttribute(FLASH_ATTRIBUTE_UPDATE_TODO, newTodoResult);

            // アドレスバーにusePrevCondition=true が残るのを避けるためリダイレクトさせる
            return new ModelAndView("redirect:/todo/");
        }

        TodoSearchForm form = getFlashAttribute(request, FLASH_ATTRIBUTE_PREV_CONDITION);
        if (form == null) {
            // usePrevCondition == false の場合
            form = new TodoSearchForm();
            form.setWord(word);
            form.setStatus(status);
            form.setDeadlineFrom(deadlineFrom);
            form.setDeadlineTo(deadlineTo);
            form.setCurrentPage(currentPage);
            form.setPageSize(pageSize);
            form.setSort(sort);
        }
        form = form == null ? new TodoSearchForm() : form;

        return list(request, session, form);
    }

    private <T> T getFlashAttribute(HttpServletRequest request, String key) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            return (T) inputFlashMap.get(key);
        }
        return null;
    }

    @PostMapping("/")
    public ModelAndView list(
        HttpServletRequest request,
        HttpSession session,
        @ModelAttribute TodoSearchForm form
    ) {
        // Todo登録成功後にリダイレクトされた場合、flashMap から登録結果を取得する
        var newTodoResult = getFlashAttribute(request, FLASH_ATTRIBUTE_UPDATE_TODO);

        // 検索条件をsession に保存（前回の検索条件が必要な場合に備える）
        session.setAttribute(SESSION_KEY_PREV_CONDITION, form);

        // 検索結果取得
        var list = todoService.findTodo(form);

        var mav = new ModelAndView("todo/list");
        mav.addObject("lwc", list);
        mav.addObject("form", form);
        mav.addObject("appPagination", new AppPagination(
            form.getCurrentPage(), list.getCount(), form.getPageSize()));
        mav.addObject(FLASH_ATTRIBUTE_UPDATE_TODO, newTodoResult);
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView newTodo() {
        var mav = new ModelAndView("todo/edit");
        mav.addObject("todo", new TodoForm());
        return mav;
    }

    @GetMapping("/edit/{todoId}")
    public ModelAndView editTodo(@PathVariable long todoId) {
        var todoEntity = todoService.findByTodoId(todoId);
        if (!todoEntity.isPresent()) {
            // 404
            ModelAndView mav = new ModelAndView("/error");
            mav.setStatus(NOT_FOUND);
            return mav;
        }
        var form = todoService.entityToForm(todoEntity.get());
        var mav = new ModelAndView("todo/edit");
        mav.addObject("todo", form);
        return mav;
    }

    @PostMapping("/upsert")
    public ModelAndView newTodoPost(
        RedirectAttributes redirectAttributes,
        @ModelAttribute TodoForm form
    ) {
        // 実行ユーザID
        // ここでは固定値だが実際にはセッションやJWT からユーザIDを算出
        long updatedBy = SYSTEM_ACCOUNT_ID;
        AppMultiResult<TodoEntity> result = todoService.upsert(form, updatedBy);

        if (!result.isSuccess()) {
            // 失敗時は再度登録画面を表示
            var mav = new ModelAndView("todo/edit");
            mav.addObject("todo", form);
            mav.addObject("errorMap", result.getMap());
            return mav;
        }
        // 成功時は一覧に戻る
        // 成功メッセージを表示するためflash attribute にセット
        redirectAttributes.addFlashAttribute(FLASH_ATTRIBUTE_UPDATE_TODO, result);
        var mav = new ModelAndView("redirect:/todo/?usePrevCondition=true");
        return mav;
    }
}
