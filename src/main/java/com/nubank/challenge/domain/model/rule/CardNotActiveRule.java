package com.nubank.challenge.domain.model.rule;

import com.nubank.challenge.domain.model.dto.TransactionAuthorization;
import com.nubank.challenge.domain.repository.AccountRepository;
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
