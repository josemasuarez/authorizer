package com.naranjax.poc.domain.exception;

import static java.lang.String.format;

public class ViolationException extends RuntimeException {
    public static final String MESSAGE_PATTERN = "Rules has not passed: %s";

    private final String violation;

    public ViolationException(final String violation) {
        super();
        this.violation = violation;
    }

    @Override
    public String getMessage() {
        return format(MESSAGE_PATTERN, violation);
    }
}
