package com.nubank.challenge.domain.model.rule;

import com.nubank.challenge.domain.model.Account;
import com.nubank.challenge.domain.model.Transaction;
import com.nubank.challenge.domain.model.TransactionBuilder;
import com.nubank.challenge.domain.model.dto.TransactionAuthorization;
import com.nubank.challenge.domain.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static com.nubank.challenge.utils.TestUtils.createTransaction;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HighFrequencySmallIntervalRuleTest {

    @InjectMocks
    private HighFrequencySmallIntervalRule rule;

    @Mock
    private AccountRepository repository;

    @Test
    @DisplayName("Given an account with a transactions list when validate then it returns true")
    public void testValidateOperationPass(){
        //GIVEN

        List<Transaction> transactions =
                List.of(createTransaction(Date.from(Instant.now()
                                                           .minus(1, ChronoUnit.MINUTES))));

        when(repository.getAccount())
                .thenReturn(new Account(true,1, transactions));
        TransactionAuthorization operation =
                new TransactionAuthorization("merchant",1, new Date());

        //WHEN
        boolean validation = rule.validate(operation);

        //THEN
        assertTrue(validation);
    }

    @Test
    @DisplayName("Given an account with a transactions list when validate then it returns true")
    public void testValidateOperationNotPass(){
        //GIVEN

        List<Transaction> transactions =
                List.of(
                        createTransaction(Date.from(Instant.now()
                                                           .minus(1, ChronoUnit.MINUTES))),
                        createTransaction(Date.from(Instant.now()
                                                           .minus(1, ChronoUnit.MINUTES))),
                        createTransaction(Date.from(Instant.now()
                                                           .minus(1, ChronoUnit.MINUTES))),
                        createTransaction(Date.from(Instant.now()
                                                           .minus(1, ChronoUnit.MINUTES)))
                        );

        when(repository.getAccount())
                .thenReturn(new Account(true,1, transactions));
        TransactionAuthorization operation =
                new TransactionAuthorization("merchant",1, new Date());

        //WHEN
        boolean validation = rule.validate(operation);

        //THEN
        assertFalse(validation);
    }
}
