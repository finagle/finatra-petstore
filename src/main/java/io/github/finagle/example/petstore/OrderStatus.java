package io.github.finagle.example.petstore;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * A Java enumeration for an Order's Status attribute.
 * The three supported statuses are: Placed, Approved, and Delivered.
 * The "code" attribute of a Status is its value (the string representation of its state).
 */
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