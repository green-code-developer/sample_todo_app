package jp.green_code.todo.domain.service;

import jp.green_code.todo.TestSupportService;
import jp.green_code.todo.domain.dto.TodoDto;
import jp.green_code.todo.domain.dto.TodoSearchDto;
import jp.green_code.todo.domain.entity.TodoEntity;
import jp.green_code.shared.dto.Validated;
import jp.green_code.shared.dto.Validated.Invalid;
import jp.green_code.shared.dto.Validated.Valid;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static jp.green_code.todo.TestSupportService.makeLongString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestTodoService {

    @Autowired
    private TodoService todoService;
    @Autowired
    private TestSupportService testSupportService;

    @AfterEach
    void after() {
        testSupportService.clean();
    }

    @BeforeEach
    void before() {
        testSupportService.clean();
    }

    @Test
    void saveSuccess() {
        var TEST = "test";
        var dto = new TodoDto();
        dto.setDetail(TEST);
        var result = todoService.save(dto);
        assertInstanceOf(Valid.class, result);
        assertEquals(TEST, ((Valid<TodoEntity>) result).value().getDetail());
    }

    @Test
    void saveFailed() {
        var dto = new TodoDto();
        // 必須のdetail が空なのでバリデーションエラーとなる
        var result = todoService.save(dto);
        assertInstanceOf(Invalid.class, result);
        assertFalse(((Invalid<TodoEntity>) result).errors().get("detail").errors().isEmpty());
    }

    @Test
    void validateAndBuildEntityOfTodoId() {
        assertTodoId(true, 0L);
        assertTodoId(false, -1L);
        assertTodoId(true, null);
    }

    void assertTodoId(boolean expectToSuccess, Long todoId) {
        var dto = new TodoDto();
        dto.setTodoId(todoId);
        dto.setDetail("a");
        var result = todoService.validateAndBuildEntity(dto);
        assertEquals(expectToSuccess, result instanceof Validated.Valid<TodoEntity>);
        if (!expectToSuccess) {
            assert result instanceof Validated.Invalid<TodoEntity>;
            assertTrue(((Validated.Invalid<TodoEntity>) result).errors().containsKey("todoId"));
        }
    }

    @Test
    void validateAndBuildEntityOfDeadline() {
        assertDeadline(true, "1900-01-01");
        assertDeadline(true, "1900-1-1");
        assertDeadline(true, "2023-12-01");
        assertDeadline(true, "2023-12-1");
        assertDeadline(true, null);
        assertDeadline(true, "");
        assertDeadline(false, "a1900-1-1");
    }

    void assertDeadline(boolean expectToSuccess, String deadline) {
        var dto = new TodoDto();
        dto.setDetail("a");
        dto.setDeadline(deadline);
        var result = todoService.validateAndBuildEntity(dto);
        assertEquals(expectToSuccess, result instanceof Validated.Valid<TodoEntity>);
        if (!expectToSuccess) {
            assert result instanceof Validated.Invalid<TodoEntity>;
            assertTrue(((Validated.Invalid<TodoEntity>) result).errors().containsKey("deadline"));
        }
    }

    @Test
    public void validateAndBuildEntityOfDetail() {
        assertionDetail(true, "1");
        assertionDetail(true, makeLongString(10));
        assertionDetail(false, null);
        assertionDetail(false, "");
        assertionDetail(false, makeLongString(101));
    }

    private void assertionDetail(boolean expectToSuccess, String detail) {
        var dto = new TodoDto();
        dto.setDetail(detail);
        var result = todoService.validateAndBuildEntity(dto);
        assertEquals(expectToSuccess, result instanceof Validated.Valid<TodoEntity>);
        if (!expectToSuccess) {
            assert result instanceof Validated.Invalid<TodoEntity>;
            assertTrue(((Validated.Invalid<TodoEntity>) result).errors().containsKey("detail"));
        }
    }

    @Test
    void validateAndBuildEntityOfStatus() {
        assertStatus(true, "DOING");
        assertStatus(false, "@");
        assertStatus(false, "doing");
        assertStatus(true, null);
        assertStatus(true, "");
    }

    void assertStatus(boolean expectToSuccess, String status) {
        var dto = new TodoDto();
        dto.setDetail("a");
        dto.setTodoStatus(status);
        var result = todoService.validateAndBuildEntity(dto);
        assertEquals(expectToSuccess, result instanceof Validated.Valid<TodoEntity>);
        if (!expectToSuccess) {
            assert result instanceof Validated.Invalid<TodoEntity>;
            assertTrue(((Validated.Invalid<TodoEntity>) result).errors().containsKey("status"));
        }
    }

    @Test
    void searchSuccess() {
        var dto = new TodoSearchDto();
        var result = todoService.search(dto);
        assertInstanceOf(Valid.class, result);
    }

    @Test
    void searchFail() {
        var dto = new TodoSearchDto();
        dto.setStatus("@");
        var result = todoService.search(dto);
        assertInstanceOf(Invalid.class, result);
    }

    @Test
    void validateSearchDtoOfWord() {
        assertWord(true, "word");
        assertWord(true, null);
        assertWord(true, "");
        assertWord(true, makeLongString(100));
        assertWord(false, makeLongString(101));
    }

    void assertWord(boolean expectToSuccess, String word) {
        var dto = new TodoSearchDto();
        dto.setWord(word);
        var result = todoService.validateSearchDto(dto);
        assertEquals(expectToSuccess, result instanceof Validated.Valid<Boolean>);
        if (!expectToSuccess) {
            assert result instanceof Validated.Invalid<Boolean>;
            assertTrue(((Validated.Invalid<Boolean>) result).errors().containsKey("word"));
        }
    }

    @Test
    void validateSearchDtoOfStatus() {
        assertSearchStatus(true, "DOING");
        assertSearchStatus(true, null);
        assertSearchStatus(true, "");
        assertSearchStatus(false, "DOING?");
    }

    void assertSearchStatus(boolean expectToSuccess, String status) {
        var dto = new TodoSearchDto();
        dto.setStatus(status);
        var result = todoService.validateSearchDto(dto);
        assertEquals(expectToSuccess, result instanceof Validated.Valid<Boolean>);
        if (!expectToSuccess) {
            assert result instanceof Validated.Invalid<Boolean>;
            assertTrue(((Validated.Invalid<Boolean>) result).errors().containsKey("status"));
        }
    }

    @Test
    void validateSearchDtoOfDeadlineFrom() {
        assertSearchDeadlineFrom(true, "1900-01-01");
        assertSearchDeadlineFrom(true, null);
        assertSearchDeadlineFrom(true, "");
        assertSearchDeadlineFrom(false, "a1900-1-1");
    }

    void assertSearchDeadlineFrom(boolean expectToSuccess, String deadline) {
        var dto = new TodoSearchDto();
        dto.setDeadlineFrom(deadline);
        var result = todoService.validateSearchDto(dto);
        assertEquals(expectToSuccess, result instanceof Validated.Valid<Boolean>);
        if (!expectToSuccess) {
            assert result instanceof Validated.Invalid<Boolean>;
            assertTrue(((Validated.Invalid<Boolean>) result).errors().containsKey("deadlineFrom"));
        }
    }


    @Test
    void validateSearchDtoOfDeadlineTo() {
        assertSearchDeadlineTo(true, "1900-01-01");
        assertSearchDeadlineTo(true, null);
        assertSearchDeadlineTo(true, "");
        assertSearchDeadlineTo(false, "a1900-1-1");
    }

    void assertSearchDeadlineTo(boolean expectToSuccess, String deadline) {
        var dto = new TodoSearchDto();
        dto.setDeadlineTo(deadline);
        var result = todoService.validateSearchDto(dto);
        assertEquals(expectToSuccess, result instanceof Validated.Valid<Boolean>);
        if (!expectToSuccess) {
            assert result instanceof Validated.Invalid<Boolean>;
            assertTrue(((Validated.Invalid<Boolean>) result).errors().containsKey("deadlineTo"));
        }
    }

    @Test
    void validateSearchDtoOfSort() {
        assertSearchSort(true, "UPDATE_DESC");
        assertSearchSort(true, null);
        assertSearchSort(true, "");
        assertSearchSort(false, "@");
    }

    void assertSearchSort(boolean expectToSuccess, String sort) {
        var dto = new TodoSearchDto();
        dto.setSort(sort);
        var result = todoService.validateSearchDto(dto);
        assertEquals(expectToSuccess, result instanceof Validated.Valid<Boolean>);
        if (!expectToSuccess) {
            assert result instanceof Validated.Invalid<Boolean>;
            assertTrue(((Validated.Invalid<Boolean>) result).errors().containsKey("sort"));
        }
    }
}
