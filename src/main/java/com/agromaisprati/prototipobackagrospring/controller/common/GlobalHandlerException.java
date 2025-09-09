package com.agromaisprati.prototipobackagrospring.controller.common;

import com.agromaisprati.prototipobackagrospring.controller.exceptions.BadRequestExceptionCustom;
import com.agromaisprati.prototipobackagrospring.controller.exceptions.ConflictException;
import com.agromaisprati.prototipobackagrospring.controller.exceptions.NotFoundException;
import com.agromaisprati.prototipobackagrospring.controller.exceptions.UnauthorizedException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * Classe responsável por capturar e tratar exceções personalizadas lançadas pelos controllers da aplicação.
 * Utiliza a anotação @RestControllerAdvice para aplicar tratamento global, garantindo que todas as exceções
 * sejam interceptadas e convertidas em respostas padronizadas.
 *
 * É fundamental o uso da classe ExceptionResponse aqui, pois ela define a estrutura da resposta enviada ao cliente
 * em caso de erro, contendo o código HTTP e a mensagem explicativa. Isso melhora a legibilidade da API,
 * facilita o consumo por aplicações front-end e mantém consistência nas respostas de erro.
 *
 * Exceções tratadas:
 * - BadRequestExceptionCustom → retorna 400 (Bad Request)
 * - NotFoundException → retorna 404 (Not Found)
 */
@RestControllerAdvice
public class GlobalHandlerException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestExceptionCustom.class)
    public ExceptionResponse badRequestExceptionCustom(BadRequestExceptionCustom ex) {
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationExceptionResponse validationException(MethodArgumentNotValidException ex) {
        ValidationExceptionResponse response = new ValidationExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid data"
        );
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            response.addError(error.getDefaultMessage());
        });
        return response;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ExceptionResponse unauthorized(UnauthorizedException ex) {
        return new ExceptionResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ExceptionResponse notFoundException(NotFoundException ex) {
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ExceptionResponse conflictException(ConflictException ex) {
        return new ExceptionResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
    }

}
