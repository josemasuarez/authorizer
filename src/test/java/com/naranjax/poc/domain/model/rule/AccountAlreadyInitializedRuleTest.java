package com.naranjax.poc.domain.model.rule;

import com.naranjax.poc.domain.repository.AccountRepository;
import com.naranjax.poc.domain.model.Account;
import com.naranjax.poc.domain.model.dto.AccountCreation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountAlreadyInitializedRuleTest {

    @InjectMocks
    private AccountAlreadyInitializedRule rule;

    @Mock
    private AccountRepository repository;

    @DisplayName("Given a null account and an account request creation "
                 + "when validate an account operation creation "
                 + "then it returns true")
    @Test
    public void testValidateOperationPass(){
        //GIVEN
        when(repository.getAccount()).thenReturn(null);
        AccountCreation operation = new AccountCreation(true,1);

        //WHEN
        boolean validation = rule.validate(operation);

        //THEN
        assertTrue(validation);
    }

    @DisplayName("Given an account instance and a account request creation "
                 + "when validate then it returns false")
    @Test
    public void testValidateOperationNotPass(){
        //GIVEN
        when(repository.getAccount())
                .thenReturn(new Account(true,1, new ArrayList<>()));
        AccountCreation operation = new AccountCreation(true,1);

        //WHEN
        boolean validation = rule.validate(operation);

        //THEN
        assertFalse(validation);
    }

}
