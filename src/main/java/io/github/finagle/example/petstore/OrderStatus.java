package io.github.finagle.example.petstore;

public enum OrderStatus {
    Placed ("placed"),
    Approved ("approved"),
    Delivered ("delivered");

    private final String code;

    OrderStatus(String code) {
        this.code = code;
    }

    public String code(){ return code; }
}