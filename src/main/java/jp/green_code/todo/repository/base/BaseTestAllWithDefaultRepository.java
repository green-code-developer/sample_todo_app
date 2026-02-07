package jp.green_code.todo.repository.base;

import java.lang.Long;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.green_code.todo.entity.TestAllWithDefaultEntity;

/**
 * Table: test_all_with_default
 */
public abstract class BaseTestAllWithDefaultRepository {

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

    public BaseTestAllWithDefaultRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    public Long upsert(TestAllWithDefaultEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into test_all_with_default");
        var insertColumns = new ArrayList<String>();
        if (entity.getColSmallint() != null) {
            insertColumns.add("col_smallint");
        }
        if (entity.getColSmallserial() != null) {
            insertColumns.add("col_smallserial");
        }
        if (entity.getColInteger() != null) {
            insertColumns.add("col_integer");
        }
        if (entity.getColSerial() != null) {
            insertColumns.add("col_serial");
        }
        if (entity.getColBigint() != null) {
            insertColumns.add("col_bigint");
        }
        if (entity.getColBigserial() != null) {
            insertColumns.add("col_bigserial");
        }
        if (entity.getColReal() != null) {
            insertColumns.add("col_real");
        }
        if (entity.getColDoublePrecision() != null) {
            insertColumns.add("col_double_precision");
        }
        if (entity.getColNumeric() != null) {
            insertColumns.add("col_numeric");
        }
        if (entity.getColBoolean() != null) {
            insertColumns.add("col_boolean");
        }
        if (entity.getColChar() != null) {
            insertColumns.add("col_char");
        }
        if (entity.getColVarchar() != null) {
            insertColumns.add("col_varchar");
        }
        if (entity.getColText() != null) {
            insertColumns.add("col_text");
        }
        if (entity.getColDate() != null) {
            insertColumns.add("col_date");
        }
        if (entity.getColTime() != null) {
            insertColumns.add("col_time");
        }
        if (entity.getColTimeTz() != null) {
            insertColumns.add("col_time_tz");
        }
        if (entity.getColTimestamp() != null) {
            insertColumns.add("col_timestamp");
        }
        if (entity.getColTimestampTz() != null) {
            insertColumns.add("col_timestamp_tz");
        }
        if (entity.getColInterval() != null) {
            insertColumns.add("col_interval");
        }
        if (entity.getColBytea() != null) {
            insertColumns.add("col_bytea");
        }
        if (entity.getColUuid() != null) {
            insertColumns.add("col_uuid");
        }
        if (entity.getColJson() != null) {
            insertColumns.add("col_json");
        }
        if (entity.getColJsonb() != null) {
            insertColumns.add("col_jsonb");
        }
        if (entity.getColXml() != null) {
            insertColumns.add("col_xml");
        }
        if (entity.getColInet() != null) {
            insertColumns.add("col_inet");
        }
        if (entity.getColCidr() != null) {
            insertColumns.add("col_cidr");
        }
        if (entity.getColMacaddr() != null) {
            insertColumns.add("col_macaddr");
        }
        if (entity.getColBox() != null) {
            insertColumns.add("col_box");
        }
        if (entity.getColPoint() != null) {
            insertColumns.add("col_point");
        }
        if (entity.getColLine() != null) {
            insertColumns.add("col_line");
        }
        if (entity.getColLseg() != null) {
            insertColumns.add("col_lseg");
        }
        if (entity.getColPath() != null) {
            insertColumns.add("col_path");
        }
        if (entity.getColPolygon() != null) {
            insertColumns.add("col_polygon");
        }
        if (entity.getColCircle() != null) {
            insertColumns.add("col_circle");
        }
        if (entity.getColTodoStatus() != null) {
            insertColumns.add("col_todo_status");
        }
        if (insertColumns.isEmpty()) {
            // 全てのカラムがデフォルト値の場合
            sql.add("DEFAULT VALUES");
        } else {
            sql.add("(%s)".formatted(String.join(", ", insertColumns)));
            sql.add("values");
            var insertValues = new ArrayList<String>();
            if (entity.getColSmallint() != null) {
                insertValues.add(":colSmallint");
            }
            if (entity.getColSmallserial() != null) {
                insertValues.add(":colSmallserial");
            }
            if (entity.getColInteger() != null) {
                insertValues.add(":colInteger");
            }
            if (entity.getColSerial() != null) {
                insertValues.add(":colSerial");
            }
            if (entity.getColBigint() != null) {
                insertValues.add(":colBigint");
            }
            if (entity.getColBigserial() != null) {
                insertValues.add(":colBigserial");
            }
            if (entity.getColReal() != null) {
                insertValues.add(":colReal");
            }
            if (entity.getColDoublePrecision() != null) {
                insertValues.add(":colDoublePrecision");
            }
            if (entity.getColNumeric() != null) {
                insertValues.add(":colNumeric");
            }
            if (entity.getColBoolean() != null) {
                insertValues.add(":colBoolean");
            }
            if (entity.getColChar() != null) {
                insertValues.add(":colChar");
            }
            if (entity.getColVarchar() != null) {
                insertValues.add(":colVarchar");
            }
            if (entity.getColText() != null) {
                insertValues.add(":colText");
            }
            if (entity.getColDate() != null) {
                insertValues.add(":colDate");
            }
            if (entity.getColTime() != null) {
                insertValues.add(":colTime");
            }
            if (entity.getColTimeTz() != null) {
                insertValues.add(":colTimeTz");
            }
            if (entity.getColTimestamp() != null) {
                insertValues.add(":colTimestamp");
            }
            if (entity.getColTimestampTz() != null) {
                insertValues.add(":colTimestampTz");
            }
            if (entity.getColInterval() != null) {
                insertValues.add("make_interval(secs => :colInterval)");
            }
            if (entity.getColBytea() != null) {
                insertValues.add(":colBytea");
            }
            if (entity.getColUuid() != null) {
                insertValues.add(":colUuid");
            }
            if (entity.getColJson() != null) {
                insertValues.add(":colJson::jsonb");
            }
            if (entity.getColJsonb() != null) {
                insertValues.add(":colJsonb::jsonb");
            }
            if (entity.getColXml() != null) {
                insertValues.add(":colXml::xml");
            }
            if (entity.getColInet() != null) {
                insertValues.add(":colInet::inet");
            }
            if (entity.getColCidr() != null) {
                insertValues.add(":colCidr::cidr");
            }
            if (entity.getColMacaddr() != null) {
                insertValues.add(":colMacaddr::macaddr");
            }
            if (entity.getColBox() != null) {
                insertValues.add(":colBox::box");
            }
            if (entity.getColPoint() != null) {
                insertValues.add(":colPoint::point");
            }
            if (entity.getColLine() != null) {
                insertValues.add(":colLine::line");
            }
            if (entity.getColLseg() != null) {
                insertValues.add(":colLseg::lseg");
            }
            if (entity.getColPath() != null) {
                insertValues.add(":colPath::path");
            }
            if (entity.getColPolygon() != null) {
                insertValues.add(":colPolygon::polygon");
            }
            if (entity.getColCircle() != null) {
                insertValues.add(":colCircle::circle");
            }
            if (entity.getColTodoStatus() != null) {
                insertValues.add(":colTodoStatus::todo_status");
            }
            sql.add("(%s)".formatted(String.join(", ", insertValues)));
                sql.add("on conflict (");
                sql.add("    col_bigserial");
            var updateValues = new ArrayList<String>();
            if (entity.getColSmallint() != null) {
                updateValues.add("col_smallint = EXCLUDED.col_smallint");
            }
            if (entity.getColSmallserial() != null) {
                updateValues.add("col_smallserial = EXCLUDED.col_smallserial");
            }
            if (entity.getColInteger() != null) {
                updateValues.add("col_integer = EXCLUDED.col_integer");
            }
            if (entity.getColSerial() != null) {
                updateValues.add("col_serial = EXCLUDED.col_serial");
            }
            if (entity.getColBigint() != null) {
                updateValues.add("col_bigint = EXCLUDED.col_bigint");
            }
            if (entity.getColReal() != null) {
                updateValues.add("col_real = EXCLUDED.col_real");
            }
            if (entity.getColDoublePrecision() != null) {
                updateValues.add("col_double_precision = EXCLUDED.col_double_precision");
            }
            if (entity.getColNumeric() != null) {
                updateValues.add("col_numeric = EXCLUDED.col_numeric");
            }
            if (entity.getColBoolean() != null) {
                updateValues.add("col_boolean = EXCLUDED.col_boolean");
            }
            if (entity.getColChar() != null) {
                updateValues.add("col_char = EXCLUDED.col_char");
            }
            if (entity.getColVarchar() != null) {
                updateValues.add("col_varchar = EXCLUDED.col_varchar");
            }
            if (entity.getColText() != null) {
                updateValues.add("col_text = EXCLUDED.col_text");
            }
            if (entity.getColDate() != null) {
                updateValues.add("col_date = EXCLUDED.col_date");
            }
            if (entity.getColTime() != null) {
                updateValues.add("col_time = EXCLUDED.col_time");
            }
            if (entity.getColTimeTz() != null) {
                updateValues.add("col_time_tz = EXCLUDED.col_time_tz");
            }
            if (entity.getColTimestamp() != null) {
                updateValues.add("col_timestamp = EXCLUDED.col_timestamp");
            }
            if (entity.getColTimestampTz() != null) {
                updateValues.add("col_timestamp_tz = EXCLUDED.col_timestamp_tz");
            }
            if (entity.getColInterval() != null) {
                updateValues.add("col_interval = EXCLUDED.col_interval");
            }
            if (entity.getColBytea() != null) {
                updateValues.add("col_bytea = EXCLUDED.col_bytea");
            }
            if (entity.getColUuid() != null) {
                updateValues.add("col_uuid = EXCLUDED.col_uuid");
            }
            if (entity.getColJson() != null) {
                updateValues.add("col_json = EXCLUDED.col_json");
            }
            if (entity.getColJsonb() != null) {
                updateValues.add("col_jsonb = EXCLUDED.col_jsonb");
            }
            if (entity.getColXml() != null) {
                updateValues.add("col_xml = EXCLUDED.col_xml");
            }
            if (entity.getColInet() != null) {
                updateValues.add("col_inet = EXCLUDED.col_inet");
            }
            if (entity.getColCidr() != null) {
                updateValues.add("col_cidr = EXCLUDED.col_cidr");
            }
            if (entity.getColMacaddr() != null) {
                updateValues.add("col_macaddr = EXCLUDED.col_macaddr");
            }
            if (entity.getColBox() != null) {
                updateValues.add("col_box = EXCLUDED.col_box");
            }
            if (entity.getColPoint() != null) {
                updateValues.add("col_point = EXCLUDED.col_point");
            }
            if (entity.getColLine() != null) {
                updateValues.add("col_line = EXCLUDED.col_line");
            }
            if (entity.getColLseg() != null) {
                updateValues.add("col_lseg = EXCLUDED.col_lseg");
            }
            if (entity.getColPath() != null) {
                updateValues.add("col_path = EXCLUDED.col_path");
            }
            if (entity.getColPolygon() != null) {
                updateValues.add("col_polygon = EXCLUDED.col_polygon");
            }
            if (entity.getColCircle() != null) {
                updateValues.add("col_circle = EXCLUDED.col_circle");
            }
            if (entity.getColTodoStatus() != null) {
                updateValues.add("col_todo_status = EXCLUDED.col_todo_status");
            }
            if (updateValues.isEmpty()) {
                sql.add(") do nothing");
            } else {
                sql.add(") do update set");
                sql.add(String.join(", ", updateValues));
            }
        }
        sql.add("returning col_bigserial");

        var param = entityToParam(entity);
        return helper.one(sql, param, Long.class).orElseThrow();
    }

    public static Map<String, Object> entityToParam(TestAllWithDefaultEntity entity) {
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

    public Optional<TestAllWithDefaultEntity> findByPk(Long colBigserial) {
        var sql = new ArrayList<String>();
        sql.add("select %s".formatted(ALL_COLUMNS));
        sql.add("from test_all_with_default");
        sql.add("where");
        sql.add("    col_bigserial = :colBigserial");

        var param = new HashMap<String, Object>();
        param.put("colBigserial", colBigserial);

        return helper.one(sql, param, TestAllWithDefaultEntity.class);
    }

    public int deleteByPk(Long colBigserial) {
        var sql = new ArrayList<String>();
        sql.add("delete from test_all_with_default");
        sql.add("where");
        sql.add("    col_bigserial = :colBigserial");

        var param = new HashMap<String, Object>();
        param.put("colBigserial", colBigserial);

        return helper.exec(sql, param);
    }
}