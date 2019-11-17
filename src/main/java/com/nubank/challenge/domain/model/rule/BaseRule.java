package com.nubank.challenge.domain.model.rule;

public abstract class BaseRule<T> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract boolean validate(T operation);
}
