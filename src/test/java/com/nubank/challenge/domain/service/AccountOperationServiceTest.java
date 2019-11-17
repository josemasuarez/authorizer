package com.nubank.challenge.domain.service;

import com.nubank.challenge.domain.model.Account;
import com.nubank.challenge.domain.model.dto.AccountCreation;
import com.nubank.challenge.domain.model.dto.OperationRequest;
import com.nubank.challenge.domain.model.dto.OperationResponse;
import com.nubank.challenge.domain.model.rule.AccountAlreadyInitializedRule;
import com.nubank.challenge.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountOperationServiceTest {

    @InjectMocks
    private AccountOperationService service;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountAlreadyInitializedRule accountAlreadyInitializedRule;

    @Test
    public void testExecuteSuccessful() {
        //GIVEN
        AccountCreation accountCreation = new AccountCreation(true, 1);
        OperationRequest request = new OperationRequest(accountCreation, null);
        when(accountRepository.getAccount()).thenReturn(new Account(true, 1, new ArrayList<>()));
        when(accountAlreadyInitializedRule.validate(accountCreation)).thenReturn(true);

        //WHEN
        OperationResponse response = service.execute(request);

        //THEN
        assertTrue(response.getAccount().isActiveCard());
        assertEquals(1, response.getAccount().getAvailableLimit());
        assertTrue(response.getViolations().isEmpty());
    }

    @Test
    public void testExecuteNotSuccessful() {
        //GIVEN
        Account account = new Account(true, 1, Collections.emptyList());
        AccountCreation accountCreation = new AccountCreation(true, 1);
        OperationRequest request = new OperationRequest(accountCreation, null);
        when(accountAlreadyInitializedRule.validate(accountCreation)).thenReturn(false);
        when(accountRepository.getAccount()).thenReturn(new Account(true, 1, new ArrayList<>()));
        when(accountAlreadyInitializedRule.getName())
                .thenReturn(AccountAlreadyInitializedRule.NAME);

        //WHEN
        OperationResponse response = service.execute(request);

        //THEN
        assertEquals(account, response.getAccount());
        assertFalse(response.getViolations().isEmpty());
        assertEquals(AccountAlreadyInitializedRule.NAME, response.getViolations().get(0));
    }

}
