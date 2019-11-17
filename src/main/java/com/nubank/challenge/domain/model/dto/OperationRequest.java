package com.nubank.challenge.domain.model.dto;

public class OperationRequest {
    private AccountCreation account;
    private TransactionAuthorization transaction;

    public OperationRequest(AccountCreation account,
                            TransactionAuthorization transaction){
        this.account = account;
        this.transaction = transaction;
    }

    public OperationRequest() {
    }

    public AccountCreation getAccount() {
        return account;
    }

    public void setAccount(AccountCreation account) {
        this.account = account;
    }

    public TransactionAuthorization getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionAuthorization transaction) {
        this.transaction = transaction;
    }
}
