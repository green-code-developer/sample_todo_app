package jp.green_code.todo;

import jp.green_code.todo.jooq.tables.pojos.Account;
import jp.green_code.todo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.StringJoiner;

@Service
public class TestSupportService {

  // テスト用アカウントID
  public static long TEST_ACCOUNT_ID_1 = -2;
  public static long TEST_ACCOUNT_ID_2 = -3;

  @Autowired
  private TestSupportRepository testSupportRepository;
  @Autowired
  private AccountRepository accountJpaRepository;

  public void clean() {
    // ForeignKey 制約に抵触しない順
    testSupportRepository.cleanTable("todo", TEST_ACCOUNT_ID_1);
    testSupportRepository.cleanTable("account", TEST_ACCOUNT_ID_1);

    // テストアカウントを再作成しておく
    addTestAccount();
  }

  public void addTestAccount() {
    Optional<Account> account1 = accountJpaRepository.findById(TEST_ACCOUNT_ID_1);
    if (account1.isEmpty()) {
      addAccount(TEST_ACCOUNT_ID_1);
    }
    Optional<Account> account2 = accountJpaRepository.findById(TEST_ACCOUNT_ID_2);
    if (account2.isEmpty()) {
      addAccount(TEST_ACCOUNT_ID_2);
    }
  }

  Account addAccount(long accountId) {
    var entity = new Account();
    entity.setAccountId(accountId);
    entity.setAccountStatus("ERR");
    entity.setName("test" + accountId);
    return accountJpaRepository.save(entity);
  }

  public static String makeLongString(int length) {
    var sb = new StringJoiner("");
    for (int i = 0; i < length; i++) {
      sb.add("あ");
    }
    return sb + "";
  }
}
