package jp.green_code.todo.util;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Component()
@RequiredArgsConstructor
public class JooqUtil {

    public static final List<String> EXCLUDE_INSERT_COLUMN_LIST = List.of("created_at");
    public static final List<String> EXCLUDE_UPDATE_COLUMN_LIST = List.of("created_at", "created_by");
    private final DSLContext dsl;

    public <R extends Record> R toRecord4Insert(Table<R> table, Object o) {
        var record = dsl.newRecord(table, o);
        EXCLUDE_INSERT_COLUMN_LIST.forEach(record::reset);
        return record;
    }

    public <R extends Record> R toRecord4Update(Table<R> table, Object o) {
        var record = dsl.newRecord(table, o);
        EXCLUDE_UPDATE_COLUMN_LIST.forEach(record::reset);
        return record;
    }

    // 汎用save
    // pk はlong 型のテーブルのみ対応
    // Insert時は作成日を、Update時は作成者と作成日を、更新しない
    // 戻り値にはreturning のpk のみ格納されている
    public <R extends Record, T> Optional<T> genericSave(Table<R> table, TableField<R, Long> pk, Class<T> clazz, T o) {
        //@formatter:off
        var res = dsl.insertInto(table).set(toRecord4Insert(table, o))
                .onConflict(pk).doUpdate().set(toRecord4Update(table, o))
                .returning(pk).fetchOneInto(clazz);
        //@formatter:on
        return ofNullable(res);
    }

    public <R extends Record, T> Optional<T> genericFindById(Table<R> table, TableField<R, Long> pk, Class<T> clazz, long id) {
        var res = dsl.select().from(table).where(pk.eq(id)).fetchOneInto(clazz);
        return ofNullable(res);
    }
}
