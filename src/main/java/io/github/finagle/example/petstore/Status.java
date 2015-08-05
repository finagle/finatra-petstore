package io.github.finagle.example.petstore;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * A Java enumeration for a pet's Status attribute. The three supported statuses are: Available, Pending, and Adopted.
 * The "code" attribute of a Status is its value (the string representation of its state).
 */
public enum Status {
    Available ("available"),
    Pending ("pending"),
    Adopted ("adopted");

    private final String code;

    Status(String code) {
        this.code = code;
    }

    @JsonValue
    /**
     * Returns the value of the Status object (available, pending, or adopted).
     */
    public String code(){ return code; }
}