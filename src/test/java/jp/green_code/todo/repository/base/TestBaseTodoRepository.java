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
        var res = repository.findByPk(id);
        data.setTodoId(id);
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4todoId(data.getTodoId(), stored.getTodoId());
        assert4todoStatus(data.getTodoStatus(), stored.getTodoStatus());
        assert4detail(data.getDetail(), stored.getDetail());
        assert4deadline(data.getDeadline(), stored.getDeadline());
        // updated_at はnow() を設定するカラムのためassertしない
        assert4updatedBy(data.getUpdatedBy(), stored.getUpdatedBy());
        // created_at はnow() を設定するカラムのためassertしない
        assert4createdBy(data.getCreatedBy(), stored.getCreatedBy());

        // update(upsert)
        seed++;
        var data2 = generateTestData(seed);
        data2.setTodoId(id);
        repository.upsert(data2);

        // select 2回目
        var res2 = repository.findByPk(id);
        assertTrue(res2.isPresent());

        // update 後の確認
        var stored2 = res2.orElseThrow();
        assert4todoId(data2.getTodoId(), stored2.getTodoId());
        assert4todoStatus(data2.getTodoStatus(), stored2.getTodoStatus());
        assert4detail(data2.getDetail(), stored2.getDetail());
        assert4deadline(data2.getDeadline(), stored2.getDeadline());
        // updated_at はnow() を設定するカラムのためassertしない
        assert4updatedBy(data2.getUpdatedBy(), stored2.getUpdatedBy());
        // created_at はupdate 対象外のため変更前の値が変わらないこと
        assert4createdAt(stored.getCreatedAt(), stored2.getCreatedAt());
        // created_by はupdate 対象外のため変更前の値が変わらないこと
        assert4createdBy(stored.getCreatedBy(), stored2.getCreatedBy());

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