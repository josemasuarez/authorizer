package com.naranjax.poc.domain.service;

import com.naranjax.poc.domain.model.Account;
import com.naranjax.poc.domain.model.dto.AccountCreation;
import com.naranjax.poc.domain.model.dto.OperationRequest;
import com.naranjax.poc.domain.model.dto.OperationResponse;
import com.naranjax.poc.domain.model.rule.AccountAlreadyInitializedRule;
import com.naranjax.poc.domain.repository.AccountRepository;
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
public class AccountServiceTest {

    @InjectMocks
    private AccountService service;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountAlreadyInitializedRule accountAlreadyInitializedRule;

    @Test
    public void testExecuteSuccessful() {
        //GIVEN
        AccountCreation accountCreation = new AccountCreation(true, 1);
        when(accountRepository.getAccount()).thenReturn(new Account(true, 1, new ArrayList<>()));
        when(accountAlreadyInitializedRule.validate(accountCreation)).thenReturn(true);

        //WHEN
        OperationResponse response = service.create(accountCreation);

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
        when(accountAlreadyInitializedRule.validate(accountCreation)).thenReturn(false);
        when(accountRepository.getAccount()).thenReturn(new Account(true, 1, new ArrayList<>()));
        when(accountAlreadyInitializedRule.getName())
                .thenReturn(AccountAlreadyInitializedRule.NAME);

        //WHEN
        OperationResponse response = service.create(accountCreation);

        //THEN
        assertEquals(account, response.getAccount());
        assertFalse(response.getViolations().isEmpty());
        assertEquals(AccountAlreadyInitializedRule.NAME, response.getViolations().get(0));
    }

}
