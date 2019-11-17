package com.nubank.challenge.domain.service;

import com.nubank.challenge.domain.model.dto.OperationRequest;
import com.nubank.challenge.domain.model.dto.OperationResponse;
import com.nubank.challenge.domain.model.dto.AccountCreation;
import com.nubank.challenge.domain.model.rule.AccountAlreadyInitializedRule;
import com.nubank.challenge.domain.model.rule.BaseRule;
import com.nubank.challenge.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountOperationService implements OperationService {

    private AccountRepository accountRepository;
    private AccountAlreadyInitializedRule accountAlreadyInitializedRule;

    public AccountOperationService(AccountRepository accountRepository,
                                   AccountAlreadyInitializedRule accountAlreadyInitializedRule) {
        this.accountRepository = accountRepository;
        this.accountAlreadyInitializedRule = accountAlreadyInitializedRule;
    }

    @Override
    public List<BaseRule> getRules() {
        return List.of(accountAlreadyInitializedRule);
    }

    @Override
    public boolean supports(OperationRequest request) {
        return request.getAccount()!= null;
    }

    @Override
    public OperationResponse execute(OperationRequest operationRequest) {

        final AccountCreation accountToCreate = operationRequest.getAccount();

        final List<String> violations =
                getRules().stream().filter(rule -> !rule.validate(accountToCreate))
                          .map(BaseRule::getName)
                          .collect(Collectors.toList());

        if (violations.isEmpty()) {
            accountRepository.createAccount(accountToCreate.isActiveCard(),
                    accountToCreate.getAvailableLimit(), new ArrayList<>());
        }

        return new OperationResponse(accountRepository.getAccount(), violations);
    }
}
