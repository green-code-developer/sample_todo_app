package jp.green_code.todo.repository;

import jp.green_code.todo.jooq.tables.pojos.Account;
import jp.green_code.todo.util.JooqUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static jp.green_code.todo.jooq.Tables.ACCOUNT;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AccountRepository {

    private final DSLContext dsl;
    private final JooqUtil jooqUtil;

    public Account save(Account data) {
        return jooqUtil.genericSave(ACCOUNT, ACCOUNT.ACCOUNT_ID, Account.class, data)
                .map(i -> {
                    data.setAccountId(i.getAccountId());
                    return data;
                }).orElseThrow();
    }

    public Optional<Account> findById(long id) {
        return jooqUtil.genericFindById(ACCOUNT, ACCOUNT.ACCOUNT_ID, Account.class, id);
    }
}
