package com.agromaisprati.prototipobackagrospring.controller.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Classe utilizada para representar a estrutura de resposta em casos de exceções personalizadas.
 * Contém informações básicas como o código de status HTTP e a mensagem de erro associada,
 * permitindo uma comunicação clara e padronizada entre o backend e o cliente.
 *
 * Exemplos de uso:
 * - Retornar uma mensagem de erro em endpoints REST quando uma exceção é capturada.
 * - Padronizar o formato de resposta para facilitar o tratamento no frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private int status;
    private String message;

}
