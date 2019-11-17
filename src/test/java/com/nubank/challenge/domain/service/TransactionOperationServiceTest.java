package com.nubank.challenge.domain.service;

import com.nubank.challenge.domain.model.Account;
import com.nubank.challenge.domain.model.Transaction;
import com.nubank.challenge.domain.model.TransactionBuilder;
import com.nubank.challenge.domain.model.dto.OperationRequest;
import com.nubank.challenge.domain.model.dto.OperationResponse;
import com.nubank.challenge.domain.model.dto.TransactionAuthorization;
import com.nubank.challenge.domain.model.rule.CardNotActiveRule;
import com.nubank.challenge.domain.model.rule.DoubledTransactionRule;
import com.nubank.challenge.domain.model.rule.HighFrequencySmallIntervalRule;
import com.nubank.challenge.domain.model.rule.InsufficientLimitRule;
import com.nubank.challenge.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionOperationServiceTest {

    @InjectMocks
    private TransactionOperationService service;

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
        final OperationRequest operationRequest =
                new OperationRequest(null, new TransactionAuthorization("merchant", 1, new Date()));

        final TransactionAuthorization transactionAuthorization =
                operationRequest.getTransaction();

        final List<Transaction> expectedAccountTransactions =
                List.of(new TransactionBuilder()
                                .setMerchant(transactionAuthorization.getMerchant())
                                .setAmount(transactionAuthorization.getAmount())
                                .setTime(transactionAuthorization.getTime())
                                .setCreatedDate(new Date())
                                .createTransaction());

        when(repository.getAccount())
                .thenReturn(account, new Account(TRUE, 0, expectedAccountTransactions));

        when(cardNotActiveRule.validate(transactionAuthorization)).thenReturn(TRUE);
        when(doubledTransactionRule.validate(transactionAuthorization)).thenReturn(TRUE);
        when(highFrequencySmallIntervalRule.validate(transactionAuthorization)).thenReturn(TRUE);
        when(insufficientLimitRule.validate(transactionAuthorization)).thenReturn(TRUE);

        //WHEN
        OperationResponse response = service.execute(operationRequest);

        //THEN
        verify(repository).createAccount(TRUE, 0, expectedAccountTransactions);
        assertTrue(response.getViolations().isEmpty());
    }

    @Test
    public void testExecuteNotSuccessful(){
        //GIVEN
        final Account account = new Account(TRUE, 1, Collections.emptyList());
        final OperationRequest operationRequest =
                new OperationRequest(null, new TransactionAuthorization("merchant", 1, new Date()));
        final TransactionAuthorization transactionAuthorization = operationRequest.getTransaction();

        when(repository.getAccount()).thenReturn(account);
        when(cardNotActiveRule.validate(transactionAuthorization)).thenReturn(FALSE);
        when(doubledTransactionRule.validate(transactionAuthorization)).thenReturn(FALSE);
        when(highFrequencySmallIntervalRule.validate(transactionAuthorization)).thenReturn(FALSE);
        when(insufficientLimitRule.validate(transactionAuthorization)).thenReturn(FALSE);

        //WHEN
        OperationResponse response = service.execute(operationRequest);

        //THEN
        assertEquals(account, response.getAccount());
        assertEquals(4, response.getViolations().size());
    }

}
