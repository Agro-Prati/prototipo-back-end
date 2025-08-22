package com.agromaisprati.prototipobackagrospring.controller;

import com.agromaisprati.prototipobackagrospring.controller.mapper.user.UserMapper;
import com.agromaisprati.prototipobackagrospring.model.user.UserDTO;
import com.agromaisprati.prototipobackagrospring.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável por expor o endpoint de registro de usuários na aplicação.
 *
 * Utiliza boas práticas REST ao retornar um status HTTP 201 (Created) com o cabeçalho `Location`,
 * indicando a URI do recurso recém-criado. Essa URI é gerada pelo método `generatorDefaultHeaderLocation`
 * da interface `GenericController`, promovendo reutilização e padronização.
 *
 * A anotação @PreAuthorize("permitAll()") garante que o endpoint esteja acessível publicamente,
 * mesmo com segurança habilitada via @EnableWebSecurity.
 *
 * A classe segue o princípio da injeção de dependência com uso de `@RequiredArgsConstructor`,
 * recebendo os serviços necessários para realizar o mapeamento e persistência do usuário.
 *
 * Endpoint exposto:
 * - POST /api/public/auth/register → registra um novo usuário
 */

@RequiredArgsConstructor
@RestController
@EnableWebSecurity
@RequestMapping("/api/")
public class UserController implements GenericController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("permitAll()")
    @PostMapping("/public/users/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserDTO response) {
        return ResponseEntity.created(generatorDefaultHeaderLocation(userService.registerUser(userMapper.toEntity(response)).getId())).build();
    }

}
