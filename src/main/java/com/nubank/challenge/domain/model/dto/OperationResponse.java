package com.nubank.challenge.domain.model.dto;

import com.nubank.challenge.domain.model.Account;

import java.util.List;

public class OperationResponse {
    private Account account;
    private List<String> violations;

    public OperationResponse(Account account, List<String> violations) {
        this.account = account;
        this.violations = violations;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<String> getViolations() {
        return violations;
    }

    public void setViolations(List<String> violations) {
        this.violations = violations;
    }
}
