package com.nubank.challenge.domain.model;

import java.util.Date;

public class TransactionBuilder {
    private String merchant;
    private int amount;
    private Date time;
    private Date createdDate;

    public TransactionBuilder setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public TransactionBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public TransactionBuilder setTime(Date time) {
        this.time = time;
        return this;
    }

    public TransactionBuilder setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Transaction createTransaction() {
        return new Transaction(merchant, amount, time, createdDate);
    }
}
