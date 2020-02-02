package com.naranjax.poc.domain.model.rule;

import com.naranjax.poc.domain.model.dto.AccountCreation;
import com.naranjax.poc.domain.repository.AccountRepository;
import org.springframework.stereotype.Component;

@Component
public class AccountAlreadyInitializedRule extends BaseRule<AccountCreation> {

    public static final String NAME = "account-already-initialized";

    private AccountRepository accountRepository;

    public AccountAlreadyInitializedRule(AccountRepository accountRepository) {
        this.setName(NAME);
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean validate(AccountCreation operation) {
        return accountRepository.getAccount() == null;
    }
}
