package com.example.todo.repository;

import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class BaseCrudRepository<PK_TYPE, ENTITY> {

  public static final String SQL_NOW = "now()";

  protected JdbcTemplate jdbcTemplate;
  protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public BaseCrudRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
  }

  abstract PK_TYPE insert(ENTITY entity, long updatedBy);

  abstract int update(ENTITY entity, long updatedBy);

  abstract int delete(PK_TYPE id);

  abstract Optional<ENTITY> findById(PK_TYPE id);
}
