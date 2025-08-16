package com.agromaisprati.prototipobackagrospring.controller.exceptions;

public class BadRequestExceptionCustom extends RuntimeException {
    public BadRequestExceptionCustom(String message) {
        super(message);
    }
}
