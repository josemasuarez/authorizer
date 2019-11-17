package com.nubank.challenge.domain.service;


import com.nubank.challenge.domain.model.dto.OperationRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperationServiceFactory {
    private final List<OperationService> services;

    public OperationServiceFactory(List<OperationService> services) {
        this.services = services;
    }

    public OperationService get(OperationRequest request){
        return services.stream()
                       .filter(service -> service.supports(request))
                       .findFirst()
                       .orElseThrow(IllegalArgumentException::new);
    }
}
