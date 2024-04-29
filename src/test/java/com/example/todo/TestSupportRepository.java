package com.example.todo;

import static com.example.todo.TestSupportService.TEST_ACCOUNT_ID_1;

import com.example.todo.entity.AccountEntity;
import com.example.todo.repository.AccountCrudRepository;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TestSupportRepository {

  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
  }

  public void cleanTable(String table, long createdBy) {
    List<String> sql = new ArrayList<>();
    sql.add("delete from " + table + " where");
    sql.add("created_by = :createdBy");
    MapSqlParameterSource param = new MapSqlParameterSource();
    param.addValue("createdBy", createdBy);
    namedParameterJdbcTemplate.update(String.join(" ", sql), param);
  }

  public long addAccount(long accountId) {
    List<String> sql = new ArrayList<>();
    // PK を指定するため、AccountCrudRepository ではinsert できない
    sql.add("insert into account (");
    sql.add("  account_id");
    sql.add("  , account_status");
    sql.add("  , name");
    sql.add("  , created_by");
    sql.add("  , updated_by");
    sql.add(") values (");
    sql.add("  :accountId");
    sql.add("  , :accountStatus");
    sql.add("  , :name");
    sql.add("  , :createdBy");
    sql.add("  , :updatedBy");
    sql.add(")");

    AccountEntity entity = new AccountEntity();
    entity.setAccountId(accountId);
    entity.setAccountStatus("ERR");
    entity.setName("test" + accountId);
    MapSqlParameterSource param = AccountCrudRepository.entityToMap(entity, TEST_ACCOUNT_ID_1);
    return namedParameterJdbcTemplate.update(String.join(" ", sql), param);
  }
}
