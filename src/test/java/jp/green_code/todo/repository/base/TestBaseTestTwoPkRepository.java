package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.lang.String;
import jp.green_code.todo.entity.TestTwoPkEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseTestTwoPkRepository {

    protected void test(BaseTestTwoPkRepository repository) {
        var seed = getInitSeed();
        var data = generateTestData(seed);

        // insert(upsert)
        repository.upsert(data);

        // select 1回目
        var stored = repository.findByPk(data.getColIntegerPk1(), data.getColIntegerPk2());
        assertTrue(stored.isPresent());
        assertEntity(data, stored.get());

        // update(upsert)
        seed++;
        var data2 = generateTestData(seed);
        repository.upsert(data2);

        // select 2回目
        var stored2 = repository.findByPk(data2.getColIntegerPk1(), data2.getColIntegerPk2());
        assertTrue(stored2.isPresent());
        assertEntity(data2, stored2.get());

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

    public TestTwoPkEntity generateTestData(int seed) {
        var entity = new TestTwoPkEntity();
        entity.setColIntegerPk1(generateTestData4colIntegerPk1(seed++));
        entity.setColIntegerPk2(generateTestData4colIntegerPk2(seed++));
        entity.setColText(generateTestData4colText(seed));
        return entity;
    }

    protected Long generateTestData4colIntegerPk1(int seed) {
        return (long) seed;
    }

    protected Long generateTestData4colIntegerPk2(int seed) {
        return (long) seed;
    }

    protected String generateTestData4colText(int seed) {
        return seed + "";
    }

    public void assertEntity(TestTwoPkEntity data, TestTwoPkEntity entity) {
        assert4colIntegerPk1(data.getColIntegerPk1(), entity.getColIntegerPk1());
        assert4colIntegerPk2(data.getColIntegerPk2(), entity.getColIntegerPk2());
        assert4colText(data.getColText(), entity.getColText());
    }

    protected void assert4colIntegerPk1(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colIntegerPk2(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colText(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}