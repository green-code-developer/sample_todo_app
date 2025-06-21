package jp.green_code.todo.repository;

import jp.green_code.todo.jooq.tables.pojos.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static jp.green_code.todo.TestSupportService.TEST_ACCOUNT_ID_1;
import static jp.green_code.todo.TestSupportService.TEST_ACCOUNT_ID_2;
import static jp.green_code.todo.enums.TodoStatusEnum.DELETED;
import static jp.green_code.todo.enums.TodoStatusEnum.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestTodoRepository {

    @Autowired
    private TodoRepository repository;

    @Test
    void crud() {
        // 初回insert
        var data = new Todo();
        var detail = "detail";
        data.setDetail(detail);
        data.setTodoStatus(NEW);
        data.setCreatedBy(TEST_ACCOUNT_ID_1);
        var todoId = repository.save(data).getTodoId();
        var inserted = repository.findById(todoId).orElseThrow();
        assertEquals(TEST_ACCOUNT_ID_1, inserted.getCreatedBy());
        assertEquals(detail, inserted.getDetail());
        assertEquals(NEW, inserted.getTodoStatus());

        // 更新
        inserted.setTodoStatus(DELETED);
        inserted.setCreatedBy(TEST_ACCOUNT_ID_2);
        repository.save(inserted);
        var updated = repository.findById(todoId).orElseThrow();
        assertEquals(DELETED, updated.getTodoStatus());
        // created_by は更新されない
        assertEquals(TEST_ACCOUNT_ID_1, updated.getCreatedBy());
        // created_at はinsert 時のまま
        assertEquals(inserted.getCreatedAt(), updated.getCreatedAt());
        // updated_at はinsert 時より未来
        assertTrue(updated.getUpdatedAt().isAfter(inserted.getUpdatedAt()));
    }
}
