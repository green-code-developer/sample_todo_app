package jp.green_code.todo.repository;

import jp.green_code.todo.jooq.Tables;
import jp.green_code.todo.jooq.tables.pojos.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

import static jp.green_code.todo.jooq.Tables.ACCOUNT;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AccountRepository {

    private final DSLContext dsl;

    public Account save(Account account) {
        var record = dsl.insertInto(ACCOUNT)
                .set(dsl.newRecord(ACCOUNT, account))
                .onConflict(ACCOUNT.ACCOUNT_ID)
                .doUpdate()
                .set(dsl.newRecord(ACCOUNT, account))
                .returning(ACCOUNT.ACCOUNT_ID)
                .fetchOne();
        return Objects.requireNonNull(record).into(Account.class);
    }

    public Optional<Account> findById(long id) {
        var res = dsl.select().from(ACCOUNT).where(ACCOUNT.ACCOUNT_ID.eq(id)).fetchOneInto(Account.class);
        return Optional.ofNullable(res);
    }
}
