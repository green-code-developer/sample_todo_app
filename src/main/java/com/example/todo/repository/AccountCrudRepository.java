package com.example.todo.repository;

import com.example.todo.entity.AccountEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class AccountCrudRepository extends BaseCrudRepository<Long, AccountEntity> {

    // テーブル名
    public static final String TABLE = "account";

    // PK のカラム名
    public static final String PK = "account_id";

    // PK を格納するEntity 上のメンバ名
    public static final String JAVA_PK = "accountId";

    @Autowired
    public AccountCrudRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Long insert(AccountEntity entity, long updatedBy) {
        List<String> sql = new ArrayList<>();
        sql.add("insert into " + TABLE + "(");
        // DDLtoEntity のinsert column 列をコピー　ここから
        // （先頭のみカンマを除く、PK は不要、created_x updated_x は不要）
        sql.add("  account_status");
        sql.add("  , name");
        // DDLtoEntity のinsert column 列をコピー　ここまで

        // created_at と updated_at はテーブルデフォルトを使用するので記載しない
        sql.add("  , created_by"); // 共通
        sql.add("  , updated_by"); // 共通
        sql.add(") values (");

        // DDLtoEntity のinsert values 列をコピー　ここから
        // （先頭のみカンマを除く、PK は不要、created_x updated_x は不要）
        sql.add("  :accountStatus");
        sql.add("  , :name");
        // DDLtoEntity のinsert values 列をコピー　ここまで

        sql.add("  , :createdBy"); // 共通
        sql.add("  , :updatedBy"); // 共通
        sql.add(") returning " + PK);
        MapSqlParameterSource param = entityToMap(entity, updatedBy);
        return namedParameterJdbcTemplate.queryForObject(String.join(" ", sql), param, Long.class);
    }

    @Override
    public int update(AccountEntity entity, long updatedBy) {
        List<String> sql = new ArrayList<>();
        sql.add("update " + TABLE + " set");
        // DDLtoEntity のupdate 列をコピー　ここから
        // （先頭のみカンマを除く、PK は不要、created_x updated_x は不要）
        sql.add("  account_status = :accountStatus");
        sql.add("  , name = :name");
        // DDLtoEntity のupdate 列をコピー　ここまで

        // update なのでcreated_x は触らない
        sql.add("  , updated_at = " + SQL_NOW); // 共通
        sql.add("  , updated_by = :updatedBy"); // 共通
        sql.add("where");
        sql.add("  " + PK + " = :" + JAVA_PK);
        MapSqlParameterSource param = entityToMap(entity, updatedBy);
        return namedParameterJdbcTemplate.update(String.join(" ", sql), param);
    }

    @Override
    public int delete(Long id) {
        List<String> sql = new ArrayList<>();
        sql.add("delete from " + TABLE);
        sql.add("where " + PK + " = :id");
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        return namedParameterJdbcTemplate.update(String.join(" ", sql), param);
    }

    @Override
    public Optional<AccountEntity> findById(Long id) {
        List<String> sql = new ArrayList<>();
        sql.add("select * from " + TABLE);
        sql.add("where " + PK + " = :id");
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        return namedParameterJdbcTemplate
            .query(String.join(" ", sql), param,
                new BeanPropertyRowMapper<>(AccountEntity.class)).stream().findFirst();
    }

    public static MapSqlParameterSource entityToMap(AccountEntity entity, long updatedBy) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        // DDLtoEntity のentityToMap 列をコピー　ここから
        // （先頭のみカンマを除く、created_x updated_x は不要）
        param.addValue("accountId", entity.getAccountId());
        param.addValue("accountStatus", entity.getAccountStatus());
        param.addValue("name", entity.getName());
        // DDLtoEntity のentityToMap 列をコピー　ここまで

        param.addValue("createdBy", updatedBy); // 共通
        param.addValue("updatedBy", updatedBy); // 共通
        return param;
    }
}