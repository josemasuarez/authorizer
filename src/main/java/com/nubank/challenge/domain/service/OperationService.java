package com.nubank.challenge.domain.service;

import com.nubank.challenge.domain.model.dto.OperationRequest;
import com.nubank.challenge.domain.model.dto.OperationResponse;
import com.nubank.challenge.domain.model.rule.BaseRule;

import java.util.List;

public interface OperationService {

    List<BaseRule> getRules();

    boolean supports(OperationRequest request);

    OperationResponse execute(OperationRequest operationRequest);
}
