package com.nubank.challenge.domain.service;

import com.nubank.challenge.domain.model.Account;
import com.nubank.challenge.domain.model.Transaction;
import com.nubank.challenge.domain.model.TransactionBuilder;
import com.nubank.challenge.domain.model.dto.OperationRequest;
import com.nubank.challenge.domain.model.dto.OperationResponse;
import com.nubank.challenge.domain.model.dto.TransactionAuthorization;
import com.nubank.challenge.domain.model.rule.BaseRule;
import com.nubank.challenge.domain.model.rule.CardNotActiveRule;
import com.nubank.challenge.domain.model.rule.DoubledTransactionRule;
import com.nubank.challenge.domain.model.rule.HighFrequencySmallIntervalRule;
import com.nubank.challenge.domain.model.rule.InsufficientLimitRule;
import com.nubank.challenge.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionOperationService implements OperationService {

    private AccountRepository accountRepository;
    private CardNotActiveRule cardNotActiveRule;
    private DoubledTransactionRule doubledTransactionRule;
    private HighFrequencySmallIntervalRule highFrequencySmallIntervalRule;
    private InsufficientLimitRule insufficientLimitRule;

    public TransactionOperationService(AccountRepository accountRepository,
                                       CardNotActiveRule cardNotActiveRule,
                                       DoubledTransactionRule doubledTransactionRule,
                                       HighFrequencySmallIntervalRule highFrequencySmallIntervalRule,
                                       InsufficientLimitRule insufficientLimitRule) {
        this.accountRepository = accountRepository;
        this.cardNotActiveRule = cardNotActiveRule;
        this.doubledTransactionRule = doubledTransactionRule;
        this.highFrequencySmallIntervalRule = highFrequencySmallIntervalRule;
        this.insufficientLimitRule = insufficientLimitRule;
    }

    @Override
    public List<BaseRule> getRules() {
        return List.of(cardNotActiveRule, doubledTransactionRule, highFrequencySmallIntervalRule,
                insufficientLimitRule);
    }

    @Override
    public boolean supports(OperationRequest request) {

        return request.getTransaction() != null;
    }

    @Override
    public OperationResponse execute(OperationRequest operationRequest) {
        final Account account = accountRepository.getAccount();
        final TransactionAuthorization transactionAuthorization = operationRequest.getTransaction();

        final List<String> violations =
                getRules().stream().filter(rule -> !rule.validate(transactionAuthorization))
                          .map(BaseRule::getName).collect(Collectors.toList());

        if (violations.isEmpty()) {
            updateAccount(account, transactionAuthorization);
        }

        return new OperationResponse(accountRepository.getAccount(), violations);
    }

    private void updateAccount(Account account, TransactionAuthorization transactionAuthorization) {
        final int newAvailableLimit =
                account.getAvailableLimit() - transactionAuthorization.getAmount();

        List<Transaction> transactions = new ArrayList<>(account.getTransactions());
        transactions.add(createTransaction(transactionAuthorization));

        accountRepository.createAccount(account.isActiveCard(), newAvailableLimit, transactions);
    }

    private Transaction createTransaction(TransactionAuthorization transactionAuthorization) {
        return new TransactionBuilder().setMerchant(transactionAuthorization.getMerchant())
                                       .setAmount(transactionAuthorization.getAmount())
                                       .setTime(transactionAuthorization.getTime())
                                       .setCreatedDate(new Date())
                                       .createTransaction();
    }
}
