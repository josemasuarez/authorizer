package com.naranjax.poc.domain.service;

import com.naranjax.poc.domain.exception.ViolationException;
import com.naranjax.poc.domain.model.Transaction;
import com.naranjax.poc.domain.model.dto.OperationRequest;
import com.naranjax.poc.domain.model.dto.OperationResponse;
import com.naranjax.poc.domain.model.dto.TransactionAuthorization;
import com.naranjax.poc.domain.model.rule.BaseRule;
import com.naranjax.poc.domain.model.rule.CardNotActiveRule;
import com.naranjax.poc.domain.model.rule.DoubledTransactionRule;
import com.naranjax.poc.domain.model.rule.HighFrequencySmallIntervalRule;
import com.naranjax.poc.domain.model.rule.InsufficientLimitRule;
import com.naranjax.poc.domain.repository.AccountRepository;
import com.naranjax.poc.domain.model.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService implements OperationService {

    private AccountRepository accountRepository;
    private CardNotActiveRule cardNotActiveRule;
    private DoubledTransactionRule doubledTransactionRule;
    private HighFrequencySmallIntervalRule highFrequencySmallIntervalRule;
    private InsufficientLimitRule insufficientLimitRule;

    @Override
    public List<BaseRule> getRules() {
        return List.of(cardNotActiveRule,
                doubledTransactionRule,
                highFrequencySmallIntervalRule,
                insufficientLimitRule);
    }

    public OperationResponse execute(TransactionAuthorization transactionAuthorization) {
        final Account account = accountRepository.getAccount();

        final List<String> violations =
                getRules().stream()
                        .filter(rule -> !rule.validate(transactionAuthorization))
                        .map(BaseRule::getName).collect(Collectors.toList());

        if (violations.isEmpty()) {
            final int newAvailableLimit =
                    account.getAvailableLimit() - transactionAuthorization.getAmount();
            updateAccount(account, newAvailableLimit, transactionAuthorization);
        } else {
            throw new ViolationException(violations.toString());
        }

        return new OperationResponse(accountRepository.getAccount(), violations);
    }

    public OperationResponse deposit(final TransactionAuthorization transactionAuthorization){
        final Account account = accountRepository.getAccount();

        final int newAvailableLimit =
                account.getAvailableLimit() + transactionAuthorization.getAmount();

        updateAccount(account, newAvailableLimit, transactionAuthorization);

        return new OperationResponse(accountRepository.getAccount(), null);
    }

    private void updateAccount(final Account account,
                               final int amount,
                               final TransactionAuthorization transactionAuthorization) {

        List<Transaction> transactions = new ArrayList<>(account.getTransactions());
        transactions.add(createTransaction(transactionAuthorization));

        accountRepository.createAccount(account.isActiveCard(), amount, transactions);
    }

    private Transaction createTransaction(TransactionAuthorization transactionAuthorization) {
        return Transaction.builder()
                .merchant(transactionAuthorization.getMerchant())
                .amount(transactionAuthorization.getAmount())
                .time(transactionAuthorization.getTime())
                .createdDate(new Date())
                .build();
    }

}
