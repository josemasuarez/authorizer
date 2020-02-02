package com.nubank.challenge.utils;

import com.naranjax.poc.domain.model.Transaction;

import java.util.Date;

public class TestUtils {
    public static Transaction createTransaction(Date createdDate) {
        return Transaction.builder().merchant("merchant")
                .amount(1)
                .time(new Date())
                .createdDate(createdDate)
                .build();
    }
}
