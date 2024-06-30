package jp.green_code.todo;

import java.util.StringJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TestSupportRepository {

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public void cleanTable(String table, long createdBy) {
    var sql = new StringJoiner(" ");
    sql.add("delete from " + table + " where");
    sql.add("created_by = :createdBy");
    var param = new MapSqlParameterSource();
    param.addValue("createdBy", createdBy);
    namedParameterJdbcTemplate.update(sql + "", param);
  }
}
