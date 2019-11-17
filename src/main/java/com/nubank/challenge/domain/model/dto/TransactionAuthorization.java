package com.nubank.challenge.domain.model.dto;

import java.util.Date;

public class TransactionAuthorization {
    private String merchant;
    private int amount;
    private Date time;

    public TransactionAuthorization() {

    }

    public TransactionAuthorization(String merchant, int amount, Date time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
