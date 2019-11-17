package com.nubank.challenge.domain.model.rule;

import com.nubank.challenge.domain.model.dto.TransactionAuthorization;
import com.nubank.challenge.domain.repository.AccountRepository;
import org.springframework.stereotype.Component;

@Component
public class InsufficientLimitRule extends BaseRule<TransactionAuthorization> {

    private static final String NAME = "insufficient-limit";

    private final AccountRepository accountRepository;

    public InsufficientLimitRule(AccountRepository accountRepository) {
        this.setName(NAME);
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean validate(TransactionAuthorization operation) {
        return operation.getAmount() <= accountRepository.getAccount().getAvailableLimit();
    }
}
