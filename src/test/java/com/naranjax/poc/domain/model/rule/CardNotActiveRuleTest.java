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
public class CardNotActiveRuleTest {

    @InjectMocks
    private CardNotActiveRule rule;

    @Mock
    private AccountRepository repository;

    @DisplayName("Given a transaction authorization when validate then it returns true")
    @Test
    public void testValidateOperationPass(){
        //GIVEN
        when(repository.getAccount())
                .thenReturn(new Account(true,1, new ArrayList<>()));
        TransactionAuthorization operation =
                new TransactionAuthorization("merchant",1, new Date());

        //WHEN
        boolean validation = rule.validate(operation);

        //THEN
        assertTrue(validation);
    }

    @DisplayName("Given a transaction authorization when validate then it returns false")
    @Test
    public void testValidateOperationNotPass(){
        //GIVEN
        when(repository.getAccount())
                .thenReturn(new Account(false,1, new ArrayList<>()));
        TransactionAuthorization operation =
                new TransactionAuthorization("merchant",1, new Date());

        //WHEN
        boolean validation = rule.validate(operation);

        //THEN
        assertFalse(validation);
    }

}
