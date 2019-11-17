package com.nubank.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Objects;

public class Transaction {
    private String merchant;
    private int amount;
    private Date time;
    private Date createdDate;

    public Transaction() {

    }

    public Transaction(String merchant, int amount, Date time, Date createdDate) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
        this.createdDate = createdDate;
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

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return amount == that.amount && Objects.equals(merchant, that.merchant) && Objects
                .equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(merchant, amount, time);
    }
}
