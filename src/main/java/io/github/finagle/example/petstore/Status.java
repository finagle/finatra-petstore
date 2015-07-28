package io.github.finagle.example.petstore;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    Available ("available"),
    Pending ("pending"),
    Adopted ("adopted");

    private final String code;

    Status(String code) {
        this.code = code;
    }

    @JsonValue
    public String code(){ return code; }
}