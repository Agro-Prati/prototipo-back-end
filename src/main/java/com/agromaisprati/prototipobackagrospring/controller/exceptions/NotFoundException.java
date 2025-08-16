package com.agromaisprati.prototipobackagrospring.controller.exceptions;

import lombok.Getter;

public class NotFoundException extends RuntimeException {
    @Getter
    public String message;

    public NotFoundException(String entity) {
        this.message = entity + " ID inserted was not possible to find the entity";
    }
}
