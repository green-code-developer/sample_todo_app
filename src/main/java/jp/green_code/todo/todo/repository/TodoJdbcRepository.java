package jp.green_code.todo.todo.repository;

import static jp.green_code.todo.todo.enums.TodoSearchSortEnum.UPDATE_DESC;
import static jp.green_code.todo.todo.repository.TodoCrudRepository.TABLE;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.isBlank;

import jp.green_code.todo.todo.entity.TodoEntity;
import jp.green_code.todo.todo.enums.TodoSearchSortEnum;
import jp.green_code.todo.todo.web.form.TodoSearchForm;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TodoJdbcRepository {

    protected JdbcTemplate jdbcTemplate;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public TodoJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
    }

    public List<TodoEntity> findByForm(TodoSearchForm form) {
        var sql = new StringJoiner(" ");
        sql.add("select * from " + TABLE);
        sql.add(toFindSql(form));
        var sort = Arrays.stream(TodoSearchSortEnum.values())
            .filter(e -> StringUtils.equals(e + "", form.getSort())).findFirst()
            .orElse(UPDATE_DESC);
        sql.add("order by " + sort.getQuery());
        sql.add("offset :offset");
        sql.add("limit :limit");
        MapSqlParameterSource param = toFindParam(form);
        return namedParameterJdbcTemplate
            .query(sql + "", param, new BeanPropertyRowMapper<>(TodoEntity.class));
    }

    public long countByForm(TodoSearchForm form) {
        var sql = new StringJoiner(" ");
        sql.add("select count(*) from " + TABLE);
        sql.add(toFindSql(form));
        MapSqlParameterSource param = toFindParam(form);
        return namedParameterJdbcTemplate.queryForObject(sql + "", param, Long.class);
    }

    String toFindSql(TodoSearchForm form) {
        var sql = new StringJoiner(" ");
        // and がどこにつくか分からないので常にtrue となる条件をつけておく
        sql.add("where 1=1");
        if (!isBlank(form.getWord())) {
            sql.add("and detail like concat('%',:word,'%')");
        }
        if (!isBlank(form.getStatus())) {
            sql.add("and todo_status = :status");
        }
        if (!isBlank(form.getDeadlineFrom())) {
            // Timestamp 比較は型を指定しないとエラーになる
            sql.add("and :deadlineFrom::timestamp <= deadline");
        }
        if (!isBlank(form.getDeadlineTo())) {
            // Timestamp 比較は型を指定しないとエラーになる
            sql.add("and deadline <= :deadlineTo::timestamp");
        }
        return sql + "";
    }

    MapSqlParameterSource toFindParam(TodoSearchForm form) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("word", form.getWord());
        param.addValue("status", form.getStatus());
        param.addValue("deadlineFrom", form.getDeadlineFrom());
        param.addValue("deadlineTo", form.getDeadlineTo());
        int offset = form.getPageSize() * (form.getCurrentPage() - 1);
        param.addValue("offset", offset);
        // 大量に取るとメモリを食い潰すので必ず上限を設定する
        int pageSize = Math.min(form.getPageSize(), 1000);
        param.addValue("limit", pageSize);
        return param;
    }

    public static MapSqlParameterSource entityToMap(TodoEntity entity, long updatedBy) {
        MapSqlParameterSource param = new MapSqlParameterSource();
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