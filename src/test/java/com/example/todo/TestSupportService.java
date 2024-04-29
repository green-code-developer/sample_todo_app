package com.example.todo;

import com.example.todo.entity.AccountEntity;
import com.example.todo.repository.AccountCrudRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestSupportService {

  // テスト用アカウントID
  public static long TEST_ACCOUNT_ID_1 = -2;
  public static long TEST_ACCOUNT_ID_2 = -3;

  private TestSupportRepository testSupportRepository;
  private AccountCrudRepository accountCrudRepository;

  @Autowired
  public TestSupportService(
      TestSupportRepository testSupportRepository,
      AccountCrudRepository accountCrudRepository) {
    this.testSupportRepository = testSupportRepository;
    this.accountCrudRepository = accountCrudRepository;
  }

  public void clean() {
    // ForeignKey 制約に抵触しない順
    testSupportRepository.cleanTable("todo", TEST_ACCOUNT_ID_1);
    testSupportRepository.cleanTable("account", TEST_ACCOUNT_ID_1);

    // テストアカウントを再作成しておく
    addTestAccount();
  }

  public void addTestAccount() {
    Optional<AccountEntity> account1 = accountCrudRepository.findById(TEST_ACCOUNT_ID_1);
    if (!account1.isPresent()) {
      testSupportRepository.addAccount(TEST_ACCOUNT_ID_1);
    }
    Optional<AccountEntity> account2 = accountCrudRepository.findById(TEST_ACCOUNT_ID_2);
    if (!account2.isPresent()) {
      testSupportRepository.addAccount(TEST_ACCOUNT_ID_2);
    }
  }
}
