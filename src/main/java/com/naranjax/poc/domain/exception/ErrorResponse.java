package com.naranjax.poc.domain.exception;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private String error;
}
