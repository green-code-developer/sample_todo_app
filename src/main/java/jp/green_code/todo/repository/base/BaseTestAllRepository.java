package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.green_code.todo.entity.TestAllEntity;

/**
 * Table: test_all
 */
public abstract class BaseTestAllRepository {

    protected final RepositoryHelper helper;
    public static final String ALL_COLUMNS = "col_smallint, col_smallserial, col_integer, col_serial, col_bigint, col_bigserial, col_real, col_double_precision, col_numeric, col_boolean, col_char, col_varchar, col_text, col_date, col_time, col_time_tz, col_timestamp, col_timestamp_tz, extract(epoch FROM col_interval) AS col_interval, col_bytea, col_uuid, col_json, col_jsonb, col_xml, col_inet, col_cidr, col_macaddr, col_box::text, col_point::text, col_line::text, col_lseg::text, col_path::text, col_polygon::text, col_circle::text, col_todo_status";

    public static final class Columns {
        public static final String COL_SMALLINT = "col_smallint";
        public static final String COL_SMALLSERIAL = "col_smallserial";
        public static final String COL_INTEGER = "col_integer";
        public static final String COL_SERIAL = "col_serial";
        public static final String COL_BIGINT = "col_bigint";
        public static final String COL_BIGSERIAL = "col_bigserial";
        public static final String COL_REAL = "col_real";
        public static final String COL_DOUBLE_PRECISION = "col_double_precision";
        public static final String COL_NUMERIC = "col_numeric";
        public static final String COL_BOOLEAN = "col_boolean";
        public static final String COL_CHAR = "col_char";
        public static final String COL_VARCHAR = "col_varchar";
        public static final String COL_TEXT = "col_text";
        public static final String COL_DATE = "col_date";
        public static final String COL_TIME = "col_time";
        public static final String COL_TIME_TZ = "col_time_tz";
        public static final String COL_TIMESTAMP = "col_timestamp";
        public static final String COL_TIMESTAMP_TZ = "col_timestamp_tz";
        public static final String COL_INTERVAL = "col_interval";
        public static final String COL_BYTEA = "col_bytea";
        public static final String COL_UUID = "col_uuid";
        public static final String COL_JSON = "col_json";
        public static final String COL_JSONB = "col_jsonb";
        public static final String COL_XML = "col_xml";
        public static final String COL_INET = "col_inet";
        public static final String COL_CIDR = "col_cidr";
        public static final String COL_MACADDR = "col_macaddr";
        public static final String COL_BOX = "col_box";
        public static final String COL_POINT = "col_point";
        public static final String COL_LINE = "col_line";
        public static final String COL_LSEG = "col_lseg";
        public static final String COL_PATH = "col_path";
        public static final String COL_POLYGON = "col_polygon";
        public static final String COL_CIRCLE = "col_circle";
        public static final String COL_TODO_STATUS = "col_todo_status";
        private Columns() {}
    }

    public BaseTestAllRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    public Long upsert(TestAllEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into test_all");
        var insertColumns = new ArrayList<String>();
        insertColumns.add("col_smallint");
        if (entity.getColSmallserial() != null) {
            insertColumns.add("col_smallserial");
        }
        insertColumns.add("col_integer");
        if (entity.getColSerial() != null) {
            insertColumns.add("col_serial");
        }
        insertColumns.add("col_bigint");
        if (entity.getColBigserial() != null) {
            insertColumns.add("col_bigserial");
        }
        insertColumns.add("col_real");
        insertColumns.add("col_double_precision");
        insertColumns.add("col_numeric");
        insertColumns.add("col_boolean");
        insertColumns.add("col_char");
        insertColumns.add("col_varchar");
        insertColumns.add("col_text");
        insertColumns.add("col_date");
        insertColumns.add("col_time");
        insertColumns.add("col_time_tz");
        insertColumns.add("col_timestamp");
        insertColumns.add("col_timestamp_tz");
        insertColumns.add("col_interval");
        insertColumns.add("col_bytea");
        insertColumns.add("col_uuid");
        insertColumns.add("col_json");
        insertColumns.add("col_jsonb");
        insertColumns.add("col_xml");
        insertColumns.add("col_inet");
        insertColumns.add("col_cidr");
        insertColumns.add("col_macaddr");
        insertColumns.add("col_box");
        insertColumns.add("col_point");
        insertColumns.add("col_line");
        insertColumns.add("col_lseg");
        insertColumns.add("col_path");
        insertColumns.add("col_polygon");
        insertColumns.add("col_circle");
        insertColumns.add("col_todo_status");
        sql.add("(%s)".formatted(String.join(", ", insertColumns)));
        sql.add("values");
        var insertValues = new ArrayList<String>();
        insertValues.add(":colSmallint");
        if (entity.getColSmallserial() != null) {
            insertValues.add(":colSmallserial");
        }
        insertValues.add(":colInteger");
        if (entity.getColSerial() != null) {
            insertValues.add(":colSerial");
        }
        insertValues.add(":colBigint");
        if (entity.getColBigserial() != null) {
            insertValues.add(":colBigserial");
        }
        insertValues.add(":colReal");
        insertValues.add(":colDoublePrecision");
        insertValues.add(":colNumeric");
        insertValues.add(":colBoolean");
        insertValues.add(":colChar");
        insertValues.add(":colVarchar");
        insertValues.add(":colText");
        insertValues.add(":colDate");
        insertValues.add(":colTime");
        insertValues.add(":colTimeTz");
        insertValues.add(":colTimestamp");
        insertValues.add(":colTimestampTz");
        insertValues.add("make_interval(secs => :colInterval)");
        insertValues.add(":colBytea");
        insertValues.add(":colUuid");
        insertValues.add(":colJson::jsonb");
        insertValues.add(":colJsonb::jsonb");
        insertValues.add(":colXml::xml");
        insertValues.add(":colInet::inet");
        insertValues.add(":colCidr::cidr");
        insertValues.add(":colMacaddr::macaddr");
        insertValues.add(":colBox::box");
        insertValues.add(":colPoint::point");
        insertValues.add(":colLine::line");
        insertValues.add(":colLseg::lseg");
        insertValues.add(":colPath::path");
        insertValues.add(":colPolygon::polygon");
        insertValues.add(":colCircle::circle");
        insertValues.add(":colTodoStatus::todo_status");
        sql.add("(%s)".formatted(String.join(", ", insertValues)));
        sql.add("on conflict (");
        sql.add("    col_bigserial");
        sql.add(") do update set");
        var updateValues = new ArrayList<String>();
        updateValues.add("col_smallint = EXCLUDED.col_smallint");
        if (entity.getColSmallserial() != null) {
            updateValues.add("col_smallserial = EXCLUDED.col_smallserial");
        }
        updateValues.add("col_integer = EXCLUDED.col_integer");
        if (entity.getColSerial() != null) {
            updateValues.add("col_serial = EXCLUDED.col_serial");
        }
        updateValues.add("col_bigint = EXCLUDED.col_bigint");
        updateValues.add("col_real = EXCLUDED.col_real");
        updateValues.add("col_double_precision = EXCLUDED.col_double_precision");
        updateValues.add("col_numeric = EXCLUDED.col_numeric");
        updateValues.add("col_boolean = EXCLUDED.col_boolean");
        updateValues.add("col_char = EXCLUDED.col_char");
        updateValues.add("col_varchar = EXCLUDED.col_varchar");
        updateValues.add("col_text = EXCLUDED.col_text");
        updateValues.add("col_date = EXCLUDED.col_date");
        updateValues.add("col_time = EXCLUDED.col_time");
        updateValues.add("col_time_tz = EXCLUDED.col_time_tz");
        updateValues.add("col_timestamp = EXCLUDED.col_timestamp");
        updateValues.add("col_timestamp_tz = EXCLUDED.col_timestamp_tz");
        updateValues.add("col_interval = EXCLUDED.col_interval");
        updateValues.add("col_bytea = EXCLUDED.col_bytea");
        updateValues.add("col_uuid = EXCLUDED.col_uuid");
        updateValues.add("col_json = EXCLUDED.col_json");
        updateValues.add("col_jsonb = EXCLUDED.col_jsonb");
        updateValues.add("col_xml = EXCLUDED.col_xml");
        updateValues.add("col_inet = EXCLUDED.col_inet");
        updateValues.add("col_cidr = EXCLUDED.col_cidr");
        updateValues.add("col_macaddr = EXCLUDED.col_macaddr");
        updateValues.add("col_box = EXCLUDED.col_box");
        updateValues.add("col_point = EXCLUDED.col_point");
        updateValues.add("col_line = EXCLUDED.col_line");
        updateValues.add("col_lseg = EXCLUDED.col_lseg");
        updateValues.add("col_path = EXCLUDED.col_path");
        updateValues.add("col_polygon = EXCLUDED.col_polygon");
        updateValues.add("col_circle = EXCLUDED.col_circle");
        updateValues.add("col_todo_status = EXCLUDED.col_todo_status");
        sql.add(String.join(", ", updateValues));
        sql.add("returning col_bigserial");

        var param = entityToParam(entity);
        return helper.one(sql, param, Long.class).orElseThrow();
    }

    public static Map<String, Object> entityToParam(TestAllEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("colSmallint", entity.getColSmallint());
        param.put("colSmallserial", entity.getColSmallserial());
        param.put("colInteger", entity.getColInteger());
        param.put("colSerial", entity.getColSerial());
        param.put("colBigint", entity.getColBigint());
        param.put("colBigserial", entity.getColBigserial());
        param.put("colReal", entity.getColReal());
        param.put("colDoublePrecision", entity.getColDoublePrecision());
        param.put("colNumeric", entity.getColNumeric());
        param.put("colBoolean", entity.getColBoolean());
        param.put("colChar", entity.getColChar());
        param.put("colVarchar", entity.getColVarchar());
        param.put("colText", entity.getColText());
        param.put("colDate", entity.getColDate());
        param.put("colTime", entity.getColTime());
        param.put("colTimeTz", entity.getColTimeTz());
        param.put("colTimestamp", entity.getColTimestamp());
        param.put("colTimestampTz", entity.getColTimestampTz());
        param.put("colInterval", entity.getColInterval());
        param.put("colBytea", entity.getColBytea());
        param.put("colUuid", entity.getColUuid());
        param.put("colJson", entity.getColJson() + "");
        param.put("colJsonb", entity.getColJsonb() + "");
        param.put("colXml", entity.getColXml());
        param.put("colInet", entity.getColInet());
        param.put("colCidr", entity.getColCidr());
        param.put("colMacaddr", entity.getColMacaddr());
        param.put("colBox", entity.getColBox());
        param.put("colPoint", entity.getColPoint());
        param.put("colLine", entity.getColLine());
        param.put("colLseg", entity.getColLseg());
        param.put("colPath", entity.getColPath());
        param.put("colPolygon", entity.getColPolygon());
        param.put("colCircle", entity.getColCircle());
        param.put("colTodoStatus", entity.getColTodoStatus() + "");
        return param;
    }

    public Optional<TestAllEntity> findByPk(Long colBigserial) {
        var sql = new ArrayList<String>();
        sql.add("select %s".formatted(ALL_COLUMNS));
        sql.add("from test_all");
        sql.add("where");
        sql.add("    col_bigserial = :colBigserial");

        var param = new HashMap<String, Object>();
        param.put("colBigserial", colBigserial);

        return helper.one(sql, param, TestAllEntity.class);
    }

    public int deleteByPk(Long colBigserial) {
        var sql = new ArrayList<String>();
        sql.add("delete from test_all");
        sql.add("where");
        sql.add("    col_bigserial = :colBigserial");

        var param = new HashMap<String, Object>();
        param.put("colBigserial", colBigserial);

        return helper.exec(sql, param);
    }
}