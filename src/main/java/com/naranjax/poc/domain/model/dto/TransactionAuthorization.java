package com.naranjax.poc.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionAuthorization {
    private String merchant;
    private int amount;
    private Date time;
}
