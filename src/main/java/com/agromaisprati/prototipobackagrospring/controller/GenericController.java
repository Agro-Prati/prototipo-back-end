package com.agromaisprati.prototipobackagrospring.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
/**
 * Interface genérica para controllers que necessitam gerar URIs de localização padrão
 * para recursos recém-criados, especialmente em respostas HTTP 201 (Created).
 *
 * O método default generatorDefaultHeaderLocation(UUID id) constrói uma URI baseada
 * no contexto atual da aplicação, apontando para o endpoint "/api/user/{id}".
 *
 * Essa abordagem é útil para incluir o cabeçalho `Location` na resposta, permitindo
 * que o cliente saiba onde acessar o recurso criado. Embora o caminho esteja fixo
 * para "/api/user/{id}", essa interface pode ser estendida ou adaptada para outros
 * tipos de recursos conforme necessário.
 *
 * Recomenda-se o uso dessa interface em controllers que seguem boas práticas RESTful,
 * promovendo consistência e reutilização de lógica comum.
 */

public interface GenericController {
    default URI generatorDefaultHeaderLocation(UUID id){
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/user/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
