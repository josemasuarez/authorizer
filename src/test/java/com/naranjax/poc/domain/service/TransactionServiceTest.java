package com.naranjax.poc.domain.service;

import com.naranjax.poc.domain.model.Account;
import com.naranjax.poc.domain.model.Transaction;
import com.naranjax.poc.domain.model.dto.OperationRequest;
import com.naranjax.poc.domain.model.dto.OperationResponse;
import com.naranjax.poc.domain.model.dto.TransactionAuthorization;
import com.naranjax.poc.domain.model.rule.CardNotActiveRule;
import com.naranjax.poc.domain.model.rule.DoubledTransactionRule;
import com.naranjax.poc.domain.model.rule.HighFrequencySmallIntervalRule;
import com.naranjax.poc.domain.model.rule.InsufficientLimitRule;
import com.naranjax.poc.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService service;

    @Mock
    private CardNotActiveRule cardNotActiveRule;

    @Mock
    private DoubledTransactionRule doubledTransactionRule;

    @Mock
    private HighFrequencySmallIntervalRule highFrequencySmallIntervalRule;

    @Mock
    private InsufficientLimitRule insufficientLimitRule;

    @Mock
    private AccountRepository repository;

    @Test
    public void testExecuteSuccessful() {
        //GIVEN
        final Account account = new Account(TRUE, 1, Collections.emptyList());

        final TransactionAuthorization transactionAuthorization =
                new TransactionAuthorization("merchant", 1, new Date());

        final List<Transaction> expectedAccountTransactions = Arrays.asList(
                Transaction.builder()
                        .merchant(transactionAuthorization.getMerchant())
                        .amount(transactionAuthorization.getAmount())
                        .time(transactionAuthorization.getTime())
                        .createdDate(new Date())
                        .build());

        when(repository.getAccount())
                .thenReturn(account, new Account(TRUE, 0, expectedAccountTransactions));

        when(cardNotActiveRule.validate(transactionAuthorization)).thenReturn(TRUE);
        when(doubledTransactionRule.validate(transactionAuthorization)).thenReturn(TRUE);
        when(highFrequencySmallIntervalRule.validate(transactionAuthorization)).thenReturn(TRUE);
        when(insufficientLimitRule.validate(transactionAuthorization)).thenReturn(TRUE);

        //WHEN
        OperationResponse response = service.execute(transactionAuthorization);

        //THEN
        verify(repository).createAccount(eq(TRUE), eq(0), anyList());
        assertTrue(response.getViolations().isEmpty());
    }

    @Test
    public void testExecuteNotSuccessful() {
        //GIVEN
        final Account account = new Account(TRUE, 1, Collections.emptyList());
        final TransactionAuthorization transactionAuthorization =
                new TransactionAuthorization("merchant", 1, new Date());

        when(repository.getAccount()).thenReturn(account);
        when(cardNotActiveRule.validate(transactionAuthorization)).thenReturn(FALSE);
        when(doubledTransactionRule.validate(transactionAuthorization)).thenReturn(FALSE);
        when(highFrequencySmallIntervalRule.validate(transactionAuthorization)).thenReturn(FALSE);
        when(insufficientLimitRule.validate(transactionAuthorization)).thenReturn(FALSE);

        //WHEN
        OperationResponse response = service.execute(transactionAuthorization);

        //THEN
        assertEquals(account, response.getAccount());
        assertEquals(4, response.getViolations().size());
    }

}
