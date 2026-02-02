package jp.green_code.todo.repository.base;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.join;

@Component
public class RepositoryHelper {
    public final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RepositoryHelper(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
    }

    public <T> List<T> list(List<String> sql, Map<String, ?> param, Class<T> clazz) {
        return list(join(" ", sql), param, clazz);
    }

    public <T> List<T> list(String sql, Map<String, ?> param, Class<T> clazz) {
        if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || clazz == String.class) {
            return namedParameterJdbcTemplate.queryForList(sql, param, clazz);
        } else {
            return namedParameterJdbcTemplate.query(sql, param, new BeanPropertyRowMapper<>(clazz));
        }
    }

    public int exec(List<String> sql, Map<String, ?> param) {
        return exec(join(" ", sql), param);
    }

    public int exec(String sql, Map<String, ?> param) {
        return namedParameterJdbcTemplate.update(sql, param);
    }

    public <T> Optional<T> one(List<String> sql, Map<String, ?> param, Class<T> clazz) {
        return list(sql, param, clazz).stream().findFirst();
    }

    public <T> Optional<T> one(String sql, Map<String, ?> param, Class<T> clazz) {
        return list(sql, param, clazz).stream().findFirst();
    }

    public long count(List<String> sql, Map<String, ?> param) {
        return count(join(" ", sql), param);
    }

    public long count(String sql, Map<String, ?> param) {
        return one(sql, param, Long.class).orElseThrow();
    }

    public static <E extends Enum<E>> E pickBySeed(Class<E> enumClass, int seed) {
        E[] values = enumClass.getEnumConstants();
        int index = Math.floorMod(seed, values.length);
        return values[index];
    }
}
