package jp.green_code.todo.repository.base;

import java.lang.Boolean;
import java.lang.Double;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Short;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.UUID;
import jp.green_code.todo.entity.TestAllWithDefaultEntity;
import jp.green_code.todo.enums.TodoStatusEnum;
import static jp.green_code.todo.repository.base.RepositoryHelper.pickBySeed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseTestAllWithDefaultRepository {

    protected void test(BaseTestAllWithDefaultRepository repository) {
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

    public TestAllWithDefaultEntity generateTestData(int seed) {
        var entity = new TestAllWithDefaultEntity();
        entity.setColSmallint(generateTestData4colSmallint(seed++));
        entity.setColSmallserial(generateTestData4colSmallserial(seed++));
        entity.setColInteger(generateTestData4colInteger(seed++));
        entity.setColSerial(generateTestData4colSerial(seed++));
        entity.setColBigint(generateTestData4colBigint(seed++));
        entity.setColBigserial(generateTestData4colBigserial(seed++));
        entity.setColReal(generateTestData4colReal(seed++));
        entity.setColDoublePrecision(generateTestData4colDoublePrecision(seed++));
        entity.setColNumeric(generateTestData4colNumeric(seed++));
        entity.setColBoolean(generateTestData4colBoolean(seed++));
        entity.setColChar(generateTestData4colChar(seed++));
        entity.setColVarchar(generateTestData4colVarchar(seed++));
        entity.setColText(generateTestData4colText(seed++));
        entity.setColDate(generateTestData4colDate(seed++));
        entity.setColTime(generateTestData4colTime(seed++));
        entity.setColTimeTz(generateTestData4colTimeTz(seed++));
        entity.setColTimestamp(generateTestData4colTimestamp(seed++));
        entity.setColTimestampTz(generateTestData4colTimestampTz(seed++));
        entity.setColInterval(generateTestData4colInterval(seed++));
        entity.setColBytea(generateTestData4colBytea(seed++));
        entity.setColUuid(generateTestData4colUuid(seed++));
        entity.setColJson(generateTestData4colJson(seed++));
        entity.setColJsonb(generateTestData4colJsonb(seed++));
        entity.setColXml(generateTestData4colXml(seed++));
        entity.setColInet(generateTestData4colInet(seed++));
        entity.setColCidr(generateTestData4colCidr(seed++));
        entity.setColMacaddr(generateTestData4colMacaddr(seed++));
        entity.setColBox(generateTestData4colBox(seed++));
        entity.setColPoint(generateTestData4colPoint(seed++));
        entity.setColLine(generateTestData4colLine(seed++));
        entity.setColLseg(generateTestData4colLseg(seed++));
        entity.setColPath(generateTestData4colPath(seed++));
        entity.setColPolygon(generateTestData4colPolygon(seed++));
        entity.setColCircle(generateTestData4colCircle(seed++));
        entity.setColTodoStatus(generateTestData4colTodoStatus(seed));
        return entity;
    }

    protected Short generateTestData4colSmallint(int seed) {
        return (short) seed;
    }

    protected Short generateTestData4colSmallserial(int seed) {
        return (short) seed;
    }

    protected Integer generateTestData4colInteger(int seed) {
        return seed;
    }

    protected Integer generateTestData4colSerial(int seed) {
        return seed;
    }

    protected Long generateTestData4colBigint(int seed) {
        return (long) seed;
    }

    protected Long generateTestData4colBigserial(int seed) {
        return (long) seed;
    }

    protected Float generateTestData4colReal(int seed) {
        return (float) seed;
    }

    protected Double generateTestData4colDoublePrecision(int seed) {
        return (double) seed;
    }

    protected BigDecimal generateTestData4colNumeric(int seed) {
        return BigDecimal.valueOf(seed);
    }

    protected Boolean generateTestData4colBoolean(int seed) {
        return seed %2 == 0;
    }

    protected String generateTestData4colChar(int seed) {
        return seed + "";
    }

    protected String generateTestData4colVarchar(int seed) {
        return seed + "";
    }

    protected String generateTestData4colText(int seed) {
        return seed + "";
    }

    protected LocalDate generateTestData4colDate(int seed) {
        return LocalDate.of(2001, 1, 1).plusDays(seed);
    }

    protected LocalTime generateTestData4colTime(int seed) {
        return LocalTime.of(0, 0, 0).plusMinutes(seed);
    }

    protected OffsetTime generateTestData4colTimeTz(int seed) {
        return OffsetTime.of(0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected LocalDateTime generateTestData4colTimestamp(int seed) {
        return LocalDateTime.of(2001, 1, 1, 0, 0, 0).plusMinutes(seed);
    }

    protected OffsetDateTime generateTestData4colTimestampTz(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected Long generateTestData4colInterval(int seed) {
        return (long) seed;
    }

    protected byte[] generateTestData4colBytea(int seed) {
        return new byte[]{(byte)(seed), (byte)(seed >> 8), (byte)(seed >> 16), (byte)(seed >> 24)};
    }

    protected UUID generateTestData4colUuid(int seed) {
        return UUID.fromString("9529478b-20d7-4232-ba79-"+String.format("%012d", seed));
    }

    protected String generateTestData4colJson(int seed) {
        return "{\"id\": %d}".formatted(seed);
    }

    protected String generateTestData4colJsonb(int seed) {
        return "{\"id\": %d}".formatted(seed);
    }

    protected String generateTestData4colXml(int seed) {
        return "<xml>%s</xml>".formatted(seed);
    }

    protected String generateTestData4colInet(int seed) {
        return String.format("%d.%d.%d.%d", (seed >> 24) & 0xFF, (seed >> 16) & 0xFF, (seed >> 8) & 0xFF, seed & 0xFF);
    }

    protected String generateTestData4colCidr(int seed) {
        return String.format("%d.%d.%d.%d/32", (seed >> 24) & 0xFF, (seed >> 16) & 0xFF, (seed >> 8) & 0xFF, seed & 0xFF);
    }

    protected String generateTestData4colMacaddr(int seed) {
        return String.format("00:00:%02x:%02x:%02x:%02x", (seed >> 24) & 0xFF, (seed >> 16) & 0xFF, (seed >> 8) & 0xFF, seed & 0xFF);
    }

    protected String generateTestData4colBox(int seed) {
        return "(1,%d),(0,0)".formatted(seed);
    }

    protected String generateTestData4colPoint(int seed) {
        return "(0,%d)".formatted(seed);
    }

    protected String generateTestData4colLine(int seed) {
        return "{1,-1,%d}".formatted(seed);
    }

    protected String generateTestData4colLseg(int seed) {
        return "[(1,%d),(0,0)]".formatted(seed);
    }

    protected String generateTestData4colPath(int seed) {
        return "((2,%d),(1,1),(0,0))".formatted(seed);
    }

    protected String generateTestData4colPolygon(int seed) {
        return "((2,%d),(1,1),(0,0))".formatted(seed);
    }

    protected String generateTestData4colCircle(int seed) {
        return "<(0,0),%d>".formatted(seed);
    }

    protected TodoStatusEnum generateTestData4colTodoStatus(int seed) {
        return pickBySeed(jp.green_code.todo.enums.TodoStatusEnum.class, seed);
    }

    public void assertEntity(TestAllWithDefaultEntity data, TestAllWithDefaultEntity entity) {
        assert4colSmallint(data.getColSmallint(), entity.getColSmallint());
        assert4colSmallserial(data.getColSmallserial(), entity.getColSmallserial());
        assert4colInteger(data.getColInteger(), entity.getColInteger());
        assert4colSerial(data.getColSerial(), entity.getColSerial());
        assert4colBigint(data.getColBigint(), entity.getColBigint());
        assert4colBigserial(data.getColBigserial(), entity.getColBigserial());
        assert4colReal(data.getColReal(), entity.getColReal());
        assert4colDoublePrecision(data.getColDoublePrecision(), entity.getColDoublePrecision());
        assert4colNumeric(data.getColNumeric(), entity.getColNumeric());
        assert4colBoolean(data.getColBoolean(), entity.getColBoolean());
        assert4colChar(data.getColChar(), entity.getColChar());
        assert4colVarchar(data.getColVarchar(), entity.getColVarchar());
        assert4colText(data.getColText(), entity.getColText());
        assert4colDate(data.getColDate(), entity.getColDate());
        assert4colTime(data.getColTime(), entity.getColTime());
        assert4colTimeTz(data.getColTimeTz(), entity.getColTimeTz());
        assert4colTimestamp(data.getColTimestamp(), entity.getColTimestamp());
        assert4colTimestampTz(data.getColTimestampTz(), entity.getColTimestampTz());
        assert4colInterval(data.getColInterval(), entity.getColInterval());
        assert4colBytea(data.getColBytea(), entity.getColBytea());
        assert4colUuid(data.getColUuid(), entity.getColUuid());
        assert4colJson(data.getColJson(), entity.getColJson());
        assert4colJsonb(data.getColJsonb(), entity.getColJsonb());
        assert4colXml(data.getColXml(), entity.getColXml());
        assert4colInet(data.getColInet(), entity.getColInet());
        assert4colCidr(data.getColCidr(), entity.getColCidr());
        assert4colMacaddr(data.getColMacaddr(), entity.getColMacaddr());
        assert4colBox(data.getColBox(), entity.getColBox());
        assert4colPoint(data.getColPoint(), entity.getColPoint());
        assert4colLine(data.getColLine(), entity.getColLine());
        assert4colLseg(data.getColLseg(), entity.getColLseg());
        assert4colPath(data.getColPath(), entity.getColPath());
        assert4colPolygon(data.getColPolygon(), entity.getColPolygon());
        assert4colCircle(data.getColCircle(), entity.getColCircle());
        assert4colTodoStatus(data.getColTodoStatus(), entity.getColTodoStatus());
    }

    protected void assert4colSmallint(Short expected, Short value) {
        assertEquals(expected, value);
    }

    protected void assert4colSmallserial(Short expected, Short value) {
        assertEquals(expected, value);
    }

    protected void assert4colInteger(Integer expected, Integer value) {
        assertEquals(expected, value);
    }

    protected void assert4colSerial(Integer expected, Integer value) {
        assertEquals(expected, value);
    }

    protected void assert4colBigint(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colBigserial(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colReal(Float expected, Float value) {
        assertEquals(expected, value);
    }

    protected void assert4colDoublePrecision(Double expected, Double value) {
        assertEquals(expected, value);
    }

    protected void assert4colNumeric(BigDecimal expected, BigDecimal value) {
        assertEquals(0, expected.compareTo(value));
    }

    protected void assert4colBoolean(Boolean expected, Boolean value) {
        assertEquals(expected, value);
    }

    protected void assert4colChar(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colVarchar(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colText(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colDate(LocalDate expected, LocalDate value) {
        assertEquals(expected, value);
    }

    protected void assert4colTime(LocalTime expected, LocalTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colTimeTz(OffsetTime expected, OffsetTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colTimestamp(LocalDateTime expected, LocalDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colTimestampTz(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colInterval(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colBytea(byte[] expected, byte[] value) {
        org.junit.jupiter.api.Assertions.assertArrayEquals(expected, value);
    }

    protected void assert4colUuid(UUID expected, UUID value) {
        assertEquals(expected, value);
    }

    protected void assert4colJson(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colJsonb(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colXml(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colInet(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colCidr(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colMacaddr(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colBox(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colPoint(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colLine(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colLseg(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colPath(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colPolygon(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colCircle(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colTodoStatus(TodoStatusEnum expected, TodoStatusEnum value) {
        assertEquals(expected, value);
    }
}