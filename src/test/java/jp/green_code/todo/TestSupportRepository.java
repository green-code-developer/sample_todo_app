package jp.green_code.todo;

import java.util.StringJoiner;
import javax.sql.DataSource;
import jp.green_code.todo.entity.AccountEntity;
import jp.green_code.todo.repository.AccountCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TestSupportRepository {

  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
  }

  public void cleanTable(String table, long createdBy) {
    var sql = new StringJoiner(" ");
    sql.add("delete from " + table + " where");
    sql.add("created_by = :createdBy");
    var param = new MapSqlParameterSource();
    param.addValue("createdBy", createdBy);
    namedParameterJdbcTemplate.update(sql + "", param);
  }

  public long addAccount(long accountId) {
    var sql = new StringJoiner(" ");
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

    var entity = new AccountEntity();
    entity.setAccountId(accountId);
    entity.setAccountStatus("ERR");
    entity.setName("test" + accountId);
    var param = AccountCrudRepository.entityToMap(entity, TestSupportService.TEST_ACCOUNT_ID_1);
    return namedParameterJdbcTemplate.update(sql + "", param);
  }
}
