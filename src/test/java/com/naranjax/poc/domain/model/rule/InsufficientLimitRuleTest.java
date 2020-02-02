package com.naranjax.poc.domain.model.rule;

import com.naranjax.poc.domain.repository.AccountRepository;
import com.naranjax.poc.domain.model.Account;
import com.naranjax.poc.domain.model.dto.TransactionAuthorization;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InsufficientLimitRuleTest {

    @InjectMocks
    private InsufficientLimitRule rule;

    @Mock
    private AccountRepository repository;

    @DisplayName("Given an account with a higher limit "
                 + "when validate a transaction authorization"
                 + "then it returns true")
    @Test
    public void testValidateOperationPass(){
        //GIVEN
        when(repository.getAccount())
                .thenReturn(new Account(true,10, new ArrayList<>()));
        TransactionAuthorization operation =
                new TransactionAuthorization("merchant",5, new Date());

        //WHEN
        boolean validation = rule.validate(operation);

        //THEN
        assertTrue(validation);
    }

    @DisplayName("Given an account with a lower limit "
                 + "when validate a transaction authorization"
                 + "then it returns false")
    @Test
    public void testValidateOperationNotPass(){
        //GIVEN
        when(repository.getAccount())
                .thenReturn(new Account(true,5, new ArrayList<>()));
        TransactionAuthorization operation =
                new TransactionAuthorization("merchant",10, new Date());

        //WHEN
        boolean validation = rule.validate(operation);

        //THEN
        assertFalse(validation);
    }

}
