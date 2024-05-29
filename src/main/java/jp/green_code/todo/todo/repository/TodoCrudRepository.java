package jp.green_code.todo.todo.repository;

import java.util.Optional;
import java.util.StringJoiner;
import javax.sql.DataSource;
import jp.green_code.todo.todo.entity.TodoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class TodoCrudRepository extends BaseCrudRepository<Long, TodoEntity> {

    // テーブル名
    public static final String TABLE = "todo";

    // PK のカラム名
    public static final String PK = "todo_id";

    // PK を格納するEntity 上のメンバ名
    public static final String JAVA_PK = "todoId";

    @Autowired
    public TodoCrudRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long insert(TodoEntity entity, long updatedBy) {
        var sql = new StringJoiner(" ");
        sql.add("insert into " + TABLE + "(");
        // DDLtoEntity のinsert column 列をコピー　ここから
        // （先頭のみカンマを除く、PK は不要、created_x updated_x は不要）
        sql.add("  todo_status");
        sql.add("  , detail");
        sql.add("  , deadline");
        // DDLtoEntity のinsert column 列をコピー　ここまで

        // created_at と updated_at はテーブルデフォルトを使用するので記載しない
        sql.add("  , created_by"); // 共通
        sql.add("  , updated_by"); // 共通
        sql.add(") values (");

        // DDLtoEntity のinsert values 列をコピー　ここから
        // （先頭のみカンマを除く、PK は不要、created_x updated_x は不要）
        sql.add("  :todoStatus");
        sql.add("  , :detail");
        sql.add("  , :deadline");
        // DDLtoEntity のinsert values 列をコピー　ここまで

        sql.add("  , :createdBy"); // 共通
        sql.add("  , :updatedBy"); // 共通
        sql.add(") returning " + PK);
        var param = entityToMap(entity, updatedBy);
        return namedParameterJdbcTemplate.queryForObject(sql + "", param, Long.class);
    }

    @Override
    public int update(TodoEntity entity, long updatedBy) {
        var sql = new StringJoiner(" ");
        sql.add("update " + TABLE + " set");
        // DDLtoEntity のupdate 列をコピー　ここから
        // （先頭のみカンマを除く、PK は不要、created_x updated_x は不要）
        sql.add("  todo_status = :todoStatus");
        sql.add("  , detail = :detail");
        sql.add("  , deadline = :deadline");
        // DDLtoEntity のupdate 列をコピー　ここまで

        // update なのでcreated_x は触らない
        sql.add("  , updated_by = :updatedBy"); // 共通
        sql.add("where");
        sql.add("  " + PK + " = :" + JAVA_PK);
        var param = entityToMap(entity, updatedBy);
        return namedParameterJdbcTemplate.update(sql + "", param);
    }

    @Override
    public int delete(Long id) {
        var sql = new StringJoiner(" ");
        sql.add("delete from " + TABLE);
        sql.add("where " + PK + " = :id");
        var param = new MapSqlParameterSource();
        param.addValue("id", id);
        return namedParameterJdbcTemplate.update(sql + "", param);
    }

    @Override
    public Optional<TodoEntity> findById(Long id) {
        var sql = new StringJoiner(" ");
        sql.add("select * from " + TABLE);
        sql.add("where " + PK + " = :id");
        var param = new MapSqlParameterSource();
        param.addValue("id", id);
        return namedParameterJdbcTemplate
            .query(sql + "", param,
                new BeanPropertyRowMapper<>(TodoEntity.class)).stream()
            .findFirst();
    }

    public static MapSqlParameterSource entityToMap(TodoEntity entity, long updatedBy) {
        var param = new MapSqlParameterSource();
        // DDLtoEntity のentityToMap 列をコピー　ここから
        // （先頭のみカンマを除く、created_x updated_x は不要）
        param.addValue("todoId", entity.getTodoId());
        param.addValue("todoStatus", entity.getTodoStatus());
        param.addValue("detail", entity.getDetail());
        param.addValue("deadline", entity.getDeadline());
        // DDLtoEntity のentityToMap 列をコピー　ここまで

        param.addValue("createdBy", updatedBy); // 共通
        param.addValue("updatedBy", updatedBy); // 共通
        return param;
    }
}