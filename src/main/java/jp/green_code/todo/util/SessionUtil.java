package jp.green_code.todo.util;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import jp.green_code.todo.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionUtil {

  public static final String SESSION_KEY_ACCOUNT = "account";

  private final HttpSession session;

  public void setLoggedAccount(AccountEntity account) {
    session.setAttribute(SESSION_KEY_ACCOUNT, account);
  }

  public Optional<AccountEntity> getLoggedAccount() {
    var sessionAccount = session.getAttribute(SESSION_KEY_ACCOUNT);
    return sessionAccount == null ? Optional.empty() : Optional.of((AccountEntity) sessionAccount);
  }
}
