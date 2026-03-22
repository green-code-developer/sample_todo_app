package jp.green_code.todo.domain.repository.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;
import jp.green_code.todo.domain.entity.TodoEntity;
import jp.green_code.todo.domain.enums.TodoStatusEnum;
import static jp.green_code.todo.domain.repository.base.BaseRepositoryHelper.pickBySeed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseTodoRepository {

    protected void test(BaseTodoRepository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        data.setTodoId(null);
        repository.insert(data);

        // select 1回目
        var res = repository.findByPk(data.getTodoId());
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4todoId(data.getTodoId(), stored.getTodoId());
        assert4todoStatus(data.getTodoStatus(), stored.getTodoStatus());
        assert4detail(data.getDetail(), stored.getDetail());
        assert4deadline(data.getDeadline(), stored.getDeadline());
        assert4updatedAt(data.getUpdatedAt(), stored.getUpdatedAt());
        assert4updatedBy(data.getUpdatedBy(), stored.getUpdatedBy());
        assert4createdAt(data.getCreatedAt(), stored.getCreatedAt());
        assert4createdBy(data.getCreatedBy(), stored.getCreatedBy());

        // update
        seed++;
        var data2 = generateTestData(seed);
        data2.setTodoId(data.getTodoId());
        repository.update(data2);

        // select 2回目
        var res2 = repository.findByPk(data2.getTodoId());
        assertTrue(res2.isPresent());

        // update 後の確認
        var stored2 = res2.orElseThrow();

        assert4todoId(data2.getTodoId(), stored2.getTodoId());

        assert4todoStatus(data2.getTodoStatus(), stored2.getTodoStatus());

        assert4detail(data2.getDetail(), stored2.getDetail());

        assert4deadline(data2.getDeadline(), stored2.getDeadline());

        assert4updatedAt(data2.getUpdatedAt(), stored2.getUpdatedAt());

        assert4updatedBy(data2.getUpdatedBy(), stored2.getUpdatedBy());

        // created_at はupdate 対象外のため変更前と変わらないことを確認
        assert4createdAt(stored.getCreatedAt(), stored2.getCreatedAt());

        // created_by はupdate 対象外のため変更前と変わらないことを確認
        assert4createdBy(stored.getCreatedBy(), stored2.getCreatedBy());

        // delete
        var deleteCount = repository.deleteByPk(data2.getTodoId());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getTodoId());
        assertTrue(stored3.isEmpty());
    }


    public TodoEntity generateTestData(int seed) {
        var entity = new TodoEntity();
        entity.setTodoId(generateTestData4todoId(seed++));
        entity.setTodoStatus(generateTestData4todoStatus(seed++));
        entity.setDetail(generateTestData4detail(seed++));
        entity.setDeadline(generateTestData4deadline(seed++));
        entity.setUpdatedAt(generateTestData4updatedAt(seed++));
        entity.setUpdatedBy(generateTestData4updatedBy(seed++));
        entity.setCreatedAt(generateTestData4createdAt(seed++));
        entity.setCreatedBy(generateTestData4createdBy(seed));
        return entity;
    }

    protected Long generateTestData4todoId(int seed) {
        return (long) seed;
    }

    protected TodoStatusEnum generateTestData4todoStatus(int seed) {
        return pickBySeed(jp.green_code.todo.domain.enums.TodoStatusEnum.class, seed);
    }

    protected String generateTestData4detail(int seed) {
        return String.valueOf(seed);
    }

    protected OffsetDateTime generateTestData4deadline(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected OffsetDateTime generateTestData4updatedAt(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected Long generateTestData4updatedBy(int seed) {
        return (long) seed;
    }

    protected OffsetDateTime generateTestData4createdAt(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected Long generateTestData4createdBy(int seed) {
        return (long) seed;
    }


    protected void assert4todoId(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4todoStatus(TodoStatusEnum expected, TodoStatusEnum value) {
        assertEquals(expected, value);
    }

    protected void assert4detail(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4deadline(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4updatedAt(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4updatedBy(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4createdAt(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4createdBy(Long expected, Long value) {
        assertEquals(expected, value);
    }
}