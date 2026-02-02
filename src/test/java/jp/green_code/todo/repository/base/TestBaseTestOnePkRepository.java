package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.lang.String;
import jp.green_code.todo.entity.TestOnePkEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseTestOnePkRepository {

    protected void test(BaseTestOnePkRepository repository) {
        var seed = getInitSeed();
        var data = generateTestData(seed);

        // insert(upsert)
        data.setColBigserial(null);
        var id = repository.upsert(data);

        // select 1回目
        var stored = repository.findByPk(id);
        data.setColBigserial(id);
        assertTrue(stored.isPresent());
        assertEntity(data, stored.get());

        // update(upsert)
        seed++;
        var data2 = generateTestData(seed);
        data2.setColBigserial(id);
        repository.upsert(data2);

        // select 2回目
        var stored2 = repository.findByPk(id);
        assertTrue(stored2.isPresent());
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

    public TestOnePkEntity generateTestData(int seed) {
        var entity = new TestOnePkEntity();
        entity.setColBigserial(generateTestData4colBigserial(seed++));
        entity.setColText(generateTestData4colText(seed));
        return entity;
    }

    protected Long generateTestData4colBigserial(int seed) {
        return (long) seed;
    }

    protected String generateTestData4colText(int seed) {
        return seed + "";
    }

    public void assertEntity(TestOnePkEntity data, TestOnePkEntity entity) {
        assert4colBigserial(data.getColBigserial(), entity.getColBigserial());
        assert4colText(data.getColText(), entity.getColText());
    }

    protected void assert4colBigserial(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colText(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}