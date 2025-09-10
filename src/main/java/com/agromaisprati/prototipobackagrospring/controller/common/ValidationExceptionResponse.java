package com.agromaisprati.prototipobackagrospring.controller.common;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationExceptionResponse extends ExceptionResponse {

    private List<String> errors = new ArrayList<>();

    public ValidationExceptionResponse(int status, String message) {
        super(status, message);
    }

    public void addError(String error) {
        this.errors.add(error);
    }

}
