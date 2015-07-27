package io.github.finagle.example.petstore;

public enum Status {
    Available ("available"),
    Pending ("pending"),
    Adopted ("adopted");

    private final String code;

    Status(String code) {
        this.code = code;
    }

    public String code(){ return code; }
}