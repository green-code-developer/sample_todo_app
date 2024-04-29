package com.example.todo.service;

import static com.example.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;
import static com.example.todo.TestSupportService.TEST_ACCOUNT_ID_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.todo.TestSupportService;
import com.example.todo.entity.TodoEntity;
import com.example.todo.util.AppMultiResult;
import com.example.todo.web.form.TodoForm;
import org.junit.jupiter.api.AfterEach;
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
        AppMultiResult result = todoService.upsert(new TodoForm(), SYSTEM_ACCOUNT_ID);
        assertEquals(false, result.isSuccess());
        assertTrue(result.getMap().containsKey("detail"));
    }

    @Test
    public void success() {
        String TEST = "test";
        TodoForm form = new TodoForm();
        form.setDetail(TEST);
        AppMultiResult<TodoEntity> result = todoService.upsert(form, TEST_ACCOUNT_ID_1);
        assertEquals(true, result.isSuccess());
        assertEquals(TEST, result.getValue().getDetail());
    }
}
