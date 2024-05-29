package jp.green_code.todo.todo.repository;

import static jp.green_code.todo.todo.enums.TodoSearchSortEnum.UPDATE_DESC;
import static jp.green_code.todo.todo.repository.TodoCrudRepository.TABLE;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import javax.sql.DataSource;
import jp.green_code.todo.todo.entity.TodoEntity;
import jp.green_code.todo.todo.enums.TodoSearchSortEnum;
import jp.green_code.todo.todo.web.form.TodoSearchForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TodoJdbcRepository {

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public TodoJdbcRepository(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
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
        var param = toFindParam(form);
        return namedParameterJdbcTemplate
            .query(sql + "", param, new BeanPropertyRowMapper<>(TodoEntity.class));
    }

    public long countByForm(TodoSearchForm form) {
        var sql = new StringJoiner(" ");
        sql.add("select count(*) from " + TABLE);
        sql.add(toFindSql(form));
        var param = toFindParam(form);
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
        var param = new MapSqlParameterSource();
        param.addValue("word", form.getWord());
        param.addValue("status", form.getStatus());
        param.addValue("deadlineFrom", form.getDeadlineFrom());
        param.addValue("deadlineTo", form.getDeadlineTo());
        param.addValue("offset", form.toOffset());
        param.addValue("limit", form.toLimit());
        return param;
    }
}