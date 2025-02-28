package com.nubank.challenge.domain.model.rule;

import com.nubank.challenge.domain.model.Account;
import com.nubank.challenge.domain.model.dto.TransactionAuthorization;
import com.nubank.challenge.domain.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class DoubledTransactionRule extends BaseRule<TransactionAuthorization> {

    private static final String NAME = "doubled-transaction";
    private static final int MAX_TRANSACTIONS = 2;
    private static final int INTERVAL = 2;

    private final AccountRepository accountRepository;

    public DoubledTransactionRule(AccountRepository accountRepository) {
        this.setName(NAME);
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean validate(TransactionAuthorization transactionAuthorization) {

        final Date from = Date.from(Instant.now().minus(INTERVAL, ChronoUnit.MINUTES));
        final Account account = accountRepository.getAccount();

        return account.getTransactions().stream()
               .filter(tr -> tr.getCreatedDate().after(from)
                             && tr.getMerchant().equals(transactionAuthorization.getMerchant())
                             && tr.getAmount() == transactionAuthorization.getAmount())
                      .count() < MAX_TRANSACTIONS;

    }
}
