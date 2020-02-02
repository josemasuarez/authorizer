package com.naranjax.poc.domain.model.rule;

import com.naranjax.poc.domain.model.dto.TransactionAuthorization;
import com.naranjax.poc.domain.repository.AccountRepository;
import org.springframework.stereotype.Component;

@Component
public class CardNotActiveRule extends BaseRule<TransactionAuthorization> {

    private static final String NAME = "card-not-active";

    private AccountRepository accountRepository;

    public CardNotActiveRule(AccountRepository accountRepository) {
        this.setName(NAME);
        this.accountRepository = accountRepository;

    }
    @Override
    public boolean validate(TransactionAuthorization operation) {
        return accountRepository.getAccount().isActiveCard();
    }
}
