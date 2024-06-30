package jp.green_code.todo.service;

import static jp.green_code.todo.TestSupportService.TEST_ACCOUNT_ID_1;
import static jp.green_code.todo.TestSupportService.makeLongString;
import static jp.green_code.todo.util.ThreadLocalUtil.setAccountId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jp.green_code.todo.TestSupportService;
import jp.green_code.todo.util.ValidationUtil;
import jp.green_code.todo.web.form.TodoForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * テストクラスではlombok は使わないの方がシンプルに書けるそうだ
 * https://stackoverflow.com/a/74028446
 */
@SpringBootTest
public class TestTodoService {

    @Autowired
    private TodoService todoService;
    @Autowired
    private TestSupportService testSupportService;
    @Autowired
    private ValidationUtil validationUtil;

    @AfterEach
    void after() {
        testSupportService.clean();
        setAccountId(TEST_ACCOUNT_ID_1);
    }

    @BeforeEach
    void before() {
        testSupportService.clean();
    }

    @Test
    void validationError() {
        // 必須であるdetail をセットせずに登録するとエラーになる
        var result = todoService.save(new TodoForm());
        assertFalse(result.getLeft().isSuccess());
        assertTrue(result.getLeft().getMap().containsKey("detail"));
    }

    @Test
    void success() {
        var TEST = "test";
        var form = new TodoForm();
        form.setDetail(TEST);
        var result = todoService.save(form);
        assertTrue(result.getLeft().isSuccess());
        assertEquals(TEST, result.getLeft().getForm().getDetail());
    }

    @Test
    void validateTodoForm() {
        assertDeadline(true, "1900-01-01");
        assertDeadline(true, "1900-1-1");
        assertDeadline(true, "2023-12-01");
        assertDeadline(true, "2023-12-1");
        assertDeadline(true, null);
        assertDeadline(true, "");
        assertDeadline(false, "a1900-1-1");
    }

    void assertDeadline(boolean expectToSuccess, String deadline) {
        var form = new TodoForm();
        form.setDetail("a");
        form.setDeadline(deadline);
        var result = validationUtil.validate(form);
        assertEquals(expectToSuccess, result.isSuccess());
        if (!expectToSuccess) {
            assertTrue(result.getMap().containsKey("deadline"));
        }
    }

    @Test
    public void validateDetail() {
        assertionDetail(true, "1");
        assertionDetail(true, makeLongString(10));
        assertionDetail(false, null);
        assertionDetail(false, "");
        assertionDetail(false, makeLongString(101));
    }

    private void assertionDetail(boolean expectToSuccess, String detail) {
        var form = new TodoForm();
        form.setDetail(detail);
        var result = validationUtil.validate(form);
        assertEquals(expectToSuccess, result.isSuccess());
        if (!expectToSuccess) {
            assertTrue(result.getMap().containsKey("detail"));
        }
    }
}
