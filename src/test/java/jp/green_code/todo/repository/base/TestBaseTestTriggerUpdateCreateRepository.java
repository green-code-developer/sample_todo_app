package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.time.OffsetDateTime;
import jp.green_code.todo.entity.TestTriggerUpdateCreateEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseTestTriggerUpdateCreateRepository {

    protected void test(BaseTestTriggerUpdateCreateRepository repository) {
        var seed = getInitSeed();
        var data = generateTestData(seed);

        // insert(upsert)
        repository.upsert(data);

        // select 1回目
        var res = repository.findByPk(data.getColIntegerPk1(), data.getColIntegerPk2());
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4colIntegerPk1(data.getColIntegerPk1(), stored.getColIntegerPk1());
        assert4colIntegerPk2(data.getColIntegerPk2(), stored.getColIntegerPk2());
        // updated_at はnow() を設定するカラムのためassertしない
        assert4updatedBy(data.getUpdatedBy(), stored.getUpdatedBy());
        // created_at はnow() を設定するカラムのためassertしない
        assert4createdBy(data.getCreatedBy(), stored.getCreatedBy());

        // update(upsert)
        seed++;
        var data2 = generateTestData(seed);
        data2.setColIntegerPk1(data.getColIntegerPk1());
        data2.setColIntegerPk2(data.getColIntegerPk2());
        repository.upsert(data2);

        // select 2回目
        var res2 = repository.findByPk(data2.getColIntegerPk1(), data2.getColIntegerPk2());
        assertTrue(res2.isPresent());

        // update 後の確認
        var stored2 = res2.orElseThrow();
        assert4colIntegerPk1(data2.getColIntegerPk1(), stored2.getColIntegerPk1());
        assert4colIntegerPk2(data2.getColIntegerPk2(), stored2.getColIntegerPk2());
        // updated_at はnow() を設定するカラムのためassertしない
        assert4updatedBy(data2.getUpdatedBy(), stored2.getUpdatedBy());
        // created_at はupdate 対象外のため変更前と変わらないことを確認
        assert4createdAt(stored.getCreatedAt(), stored2.getCreatedAt());
        // created_by はupdate 対象外のため変更前と変わらないことを確認
        assert4createdBy(stored.getCreatedBy(), stored2.getCreatedBy());

        // delete
        var deleteCount = repository.deleteByPk(data2.getColIntegerPk1(), data2.getColIntegerPk2());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getColIntegerPk1(), data2.getColIntegerPk2());
        assertTrue(stored3.isEmpty());
    }

    protected int getInitSeed() {
        return 1;
    }

    public TestTriggerUpdateCreateEntity generateTestData(int seed) {
        var entity = new TestTriggerUpdateCreateEntity();
        entity.setColIntegerPk1(generateTestData4colIntegerPk1(seed++));
        entity.setColIntegerPk2(generateTestData4colIntegerPk2(seed++));
        entity.setUpdatedAt(generateTestData4updatedAt(seed++));
        entity.setUpdatedBy(generateTestData4updatedBy(seed++));
        entity.setCreatedAt(generateTestData4createdAt(seed++));
        entity.setCreatedBy(generateTestData4createdBy(seed));
        return entity;
    }

    protected Long generateTestData4colIntegerPk1(int seed) {
        return (long) seed;
    }

    protected Long generateTestData4colIntegerPk2(int seed) {
        return (long) seed;
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


    protected void assert4colIntegerPk1(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colIntegerPk2(Long expected, Long value) {
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