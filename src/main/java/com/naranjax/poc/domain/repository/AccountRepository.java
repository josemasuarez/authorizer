package com.naranjax.poc.domain.repository;

import com.naranjax.poc.domain.model.Account;
import com.naranjax.poc.domain.model.Transaction;
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
