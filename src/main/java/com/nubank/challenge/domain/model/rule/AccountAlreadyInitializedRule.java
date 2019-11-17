package com.nubank.challenge.domain.model.rule;

import com.nubank.challenge.domain.model.dto.AccountCreation;
import com.nubank.challenge.domain.repository.AccountRepository;
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
