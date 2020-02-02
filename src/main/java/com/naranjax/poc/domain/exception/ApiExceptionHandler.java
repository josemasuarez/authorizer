package com.naranjax.poc.domain.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.servlet.DispatcherServlet.EXCEPTION_ATTRIBUTE;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ViolationException.class)
    public ResponseEntity<ErrorResponse> handle(final ViolationException exception,
                                                final HttpServletRequest request) {

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .error("violation_error")
                .build();

        request.setAttribute(EXCEPTION_ATTRIBUTE, exception);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

}
