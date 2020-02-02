package com.naranjax.poc.web;

import com.naranjax.poc.domain.model.dto.AccountCreation;
import com.naranjax.poc.domain.model.dto.OperationResponse;
import com.naranjax.poc.domain.model.dto.TransactionAuthorization;
import com.naranjax.poc.domain.service.AccountService;
import com.naranjax.poc.domain.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private TransactionService transactionService;

    private AccountService accountService;

    @PostMapping
    public ResponseEntity<OperationResponse> create(@RequestBody final AccountCreation accountCreation) {
        final OperationResponse response = accountService.create(accountCreation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deposits")
    public ResponseEntity<OperationResponse> deposit(
            @RequestBody final TransactionAuthorization transactionAuthorization) {
        final OperationResponse response = transactionService.deposit(transactionAuthorization);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/authorizations")
    public ResponseEntity<OperationResponse> authorize(
            @RequestBody final TransactionAuthorization transactionAuthorization) {

        final OperationResponse response = transactionService.execute(transactionAuthorization);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
