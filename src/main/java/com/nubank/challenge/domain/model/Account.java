package com.nubank.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Objects;

public final class Account {

    private final boolean activeCard;
    private final int availableLimit;
    private final List<Transaction> transactions;

    public Account(final boolean activeCard, final int availableLimit,
                    final List<Transaction> transactions) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
        this.transactions = transactions;
    }

    public boolean isActiveCard() {
        return activeCard;
    }

    public int getAvailableLimit() {
        return availableLimit;
    }

    @JsonIgnore
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return activeCard == account.activeCard && availableLimit == account.availableLimit
               && Objects.equals(transactions, account.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeCard, availableLimit, transactions);
    }
}
