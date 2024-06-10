package jp.green_code.todo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jp.green_code.todo.TestSupportService;
import jp.green_code.todo.web.form.TodoForm;
import jp.green_code.todo.util.AppConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTodoService {

    private TodoService todoService;
    private TestSupportService testSupportService;

    @Autowired
    public TestTodoService(
        TodoService todoService,
        TestSupportService testSupportService
    ) {
        this.todoService = todoService;
        this.testSupportService = testSupportService;
    }

    @AfterEach
    void after() {
        testSupportService.clean();
    }

    @BeforeEach
    void before() {
        testSupportService.clean();
    }

    @Test
    public void validationError() {
        // 必須であるdetail をセットせずに登録するとエラーになる
        var result = todoService.upsert(new TodoForm(), AppConstants.SYSTEM_ACCOUNT_ID);
        Assertions.assertEquals(false, result.getLeft().isSuccess());
        Assertions.assertTrue(result.getLeft().getMap().containsKey("detail"));
    }

    @Test
    public void success() {
        var TEST = "test";
        var form = new TodoForm();
        form.setDetail(TEST);
        var result = todoService.upsert(form, TestSupportService.TEST_ACCOUNT_ID_1);
        Assertions.assertEquals(true, result.getLeft().isSuccess());
        Assertions.assertEquals(TEST, result.getLeft().getValue().getDetail());
    }
}
