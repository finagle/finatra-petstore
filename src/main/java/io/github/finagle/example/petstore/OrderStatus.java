package io.github.finagle.example.petstore;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    Placed ("placed"),
    Approved ("approved"),
    Delivered ("delivered");

    private final String code;

    OrderStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String code(){ return code; }
}