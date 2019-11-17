package com.nubank.challenge.domain.repository;

import com.nubank.challenge.domain.model.Account;
import com.nubank.challenge.domain.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountRepository {
    private Account account;

    public Account getAccount() {
        return this.account;
    }

    public void createAccount(final boolean activeCard,
                                 final int availableLimit,
                                 final List<Transaction> transactions) {
        this.account = new Account(activeCard, availableLimit, transactions);
    }
}
