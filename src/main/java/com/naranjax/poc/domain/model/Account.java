package com.naranjax.poc.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public final class Account {

    private final boolean activeCard;
    private final int availableLimit;
    private final List<Transaction> transactions;
}
