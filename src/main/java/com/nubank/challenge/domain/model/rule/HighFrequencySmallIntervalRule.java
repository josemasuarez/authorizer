package com.nubank.challenge.domain.model.rule;

import com.nubank.challenge.domain.model.dto.TransactionAuthorization;
import com.nubank.challenge.domain.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class HighFrequencySmallIntervalRule extends BaseRule<TransactionAuthorization> {

    private static final String NAME = "high-frequency-small-interval";
    private static final int MAX_TRANSACTIONS = 3;
    private static final int INTERVAL = 2;

    private final AccountRepository accountRepository;

    public HighFrequencySmallIntervalRule(AccountRepository accountRepository) {
        this.setName(NAME);
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean validate(TransactionAuthorization operation) {

        final Date from = Date.from(Instant.now().minus(INTERVAL, ChronoUnit.MINUTES));

        return accountRepository.getAccount().getTransactions().stream()
                         .filter(tr -> tr.getCreatedDate().after(from))
                                .count() < MAX_TRANSACTIONS;
    }
}
