package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;
import jp.green_code.todo.entity.TodoEntity;
import jp.green_code.todo.enums.TodoStatusEnum;
import static jp.green_code.todo.repository.base.RepositoryHelper.pickBySeed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseTodoRepository {

    protected void test(BaseTodoRepository repository) {
        var seed = getInitSeed();
        var data = generateTestData(seed);

        // insert(upsert)
        data.setTodoId(null);
        var id = repository.upsert(data);

        // select 1回目
        var stored = repository.findByPk(id);
        data.setTodoId(id);
        assertTrue(stored.isPresent());
        // INSERT 対象外カラムの値を上書き（テスト不可）
        data.setCreatedAt(stored.get().getCreatedAt());
        assertEntity(data, stored.get());

        // update(upsert)
        seed++;
        var data2 = generateTestData(seed);
        data2.setTodoId(id);
        repository.upsert(data2);

        // select 2回目
        var stored2 = repository.findByPk(id);
        assertTrue(stored2.isPresent());
        // UPDATE 対象外カラムの値を更新前の値で上書き（変わっていないことを確認）
        data2.setCreatedAt(stored.get().getCreatedAt());
        data2.setCreatedBy(stored.get().getCreatedBy());
        assertEntity(data2, stored2.get());

        // delete
        var deleteCount = repository.deleteByPk(id);
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(id);
        assertTrue(stored3.isEmpty());
    }

    protected int getInitSeed() {
        return 1;
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
        return pickBySeed(jp.green_code.todo.enums.TodoStatusEnum.class, seed);
    }

    protected String generateTestData4detail(int seed) {
        return seed + "";
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

    public void assertEntity(TodoEntity data, TodoEntity entity) {
        assert4todoId(data.getTodoId(), entity.getTodoId());
        assert4todoStatus(data.getTodoStatus(), entity.getTodoStatus());
        assert4detail(data.getDetail(), entity.getDetail());
        assert4deadline(data.getDeadline(), entity.getDeadline());
        assert4updatedBy(data.getUpdatedBy(), entity.getUpdatedBy());
        assert4createdAt(data.getCreatedAt(), entity.getCreatedAt());
        assert4createdBy(data.getCreatedBy(), entity.getCreatedBy());
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