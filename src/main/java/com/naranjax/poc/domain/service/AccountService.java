package com.naranjax.poc.domain.service;

import com.naranjax.poc.domain.model.Account;
import com.naranjax.poc.domain.model.Transaction;
import com.naranjax.poc.domain.model.dto.AccountCreation;
import com.naranjax.poc.domain.model.dto.DepositRequest;
import com.naranjax.poc.domain.model.dto.OperationRequest;
import com.naranjax.poc.domain.model.dto.OperationResponse;
import com.naranjax.poc.domain.model.dto.TransactionAuthorization;
import com.naranjax.poc.domain.model.rule.AccountAlreadyInitializedRule;
import com.naranjax.poc.domain.model.rule.BaseRule;
import com.naranjax.poc.domain.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService implements OperationService {

    private AccountRepository accountRepository;
    private AccountAlreadyInitializedRule accountAlreadyInitializedRule;

    @Override
    public List<BaseRule> getRules() {
        return List.of(accountAlreadyInitializedRule);
    }

    public OperationResponse create(AccountCreation accountCreation) {

        final List<String> violations =
                getRules().stream().filter(rule -> !rule.validate(accountCreation))
                        .map(BaseRule::getName)
                        .collect(Collectors.toList());

        if (violations.isEmpty()) {
            accountRepository.createAccount(accountCreation.isActiveCard(),
                    accountCreation.getAvailableLimit(), new ArrayList<>());
        }

        return new OperationResponse(accountRepository.getAccount(), violations);
    }

}
