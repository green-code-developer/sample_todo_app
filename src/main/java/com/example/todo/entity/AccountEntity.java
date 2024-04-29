package com.example.todo.entity;

import static com.example.todo.util.AppConstants.SYSTEM_ACCOUNT_ID;

public class AccountEntity extends BaseEntity {

    private Long accountId = SYSTEM_ACCOUNT_ID;
    private String accountStatus = "";
    private String name = "";

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
