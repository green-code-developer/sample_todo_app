package jp.green_code.todo.util;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import jp.green_code.todo.jooq.tables.pojos.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionUtil {

    public static final String SESSION_KEY_ACCOUNT = "account";

    private final HttpSession session;

    public void setLoggedAccount(Account account) {
        session.setAttribute(SESSION_KEY_ACCOUNT, account);
    }

    public Optional<Account> getLoggedAccount() {
        var sessionAccount = session.getAttribute(SESSION_KEY_ACCOUNT);
        return sessionAccount == null ? Optional.empty() : Optional.of((Account) sessionAccount);
    }
}
