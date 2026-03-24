package org.example.exception;

public record ApiFieldError(
    String field,
    String message
) {}
