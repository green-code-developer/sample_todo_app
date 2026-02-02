package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;
import jp.green_code.todo.entity.AccountEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseAccountRepository {

    protected void test(BaseAccountRepository repository) {
        var seed = getInitSeed();
        var data = generateTestData(seed);

        // insert(upsert)
        data.setAccountId(null);
        var id = repository.upsert(data);

        // select 1回目
        var stored = repository.findByPk(id);
        data.setAccountId(id);
        assertTrue(stored.isPresent());
        // INSERT 対象外カラムの値を上書き（テスト不可）
        data.setCreatedAt(stored.get().getCreatedAt());
        assertEntity(data, stored.get());

        // update(upsert)
        seed++;
        var data2 = generateTestData(seed);
        data2.setAccountId(id);
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

    public AccountEntity generateTestData(int seed) {
        var entity = new AccountEntity();
        entity.setAccountId(generateTestData4accountId(seed++));
        entity.setAccountStatus(generateTestData4accountStatus(seed++));
        entity.setName(generateTestData4name(seed++));
        entity.setUpdatedAt(generateTestData4updatedAt(seed++));
        entity.setUpdatedBy(generateTestData4updatedBy(seed++));
        entity.setCreatedAt(generateTestData4createdAt(seed++));
        entity.setCreatedBy(generateTestData4createdBy(seed));
        return entity;
    }

    protected Long generateTestData4accountId(int seed) {
        return (long) seed;
    }

    protected String generateTestData4accountStatus(int seed) {
        return seed + "";
    }

    protected String generateTestData4name(int seed) {
        return seed + "";
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

    public void assertEntity(AccountEntity data, AccountEntity entity) {
        assert4accountId(data.getAccountId(), entity.getAccountId());
        assert4accountStatus(data.getAccountStatus(), entity.getAccountStatus());
        assert4name(data.getName(), entity.getName());
        assert4updatedBy(data.getUpdatedBy(), entity.getUpdatedBy());
        assert4createdAt(data.getCreatedAt(), entity.getCreatedAt());
        assert4createdBy(data.getCreatedBy(), entity.getCreatedBy());
    }

    protected void assert4accountId(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4accountStatus(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4name(String expected, String value) {
        assertEquals(expected, value.trim());
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