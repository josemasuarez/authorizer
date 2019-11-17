package com.nubank.challenge.utils;

import com.nubank.challenge.domain.model.Transaction;
import com.nubank.challenge.domain.model.TransactionBuilder;

import java.util.Date;

public class TestUtils {
    public static Transaction createTransaction(Date createdDate) {
        return new TransactionBuilder().setMerchant("merchant")
                                       .setAmount(1)
                                       .setTime(new Date())
                                       .setCreatedDate(createdDate)
                                       .createTransaction();
    }
}
