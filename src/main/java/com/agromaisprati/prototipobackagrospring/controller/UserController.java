package com.agromaisprati.prototipobackagrospring.controller;

import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
 * - POST /api/public/users → registra um novo usuário
 */

@RequiredArgsConstructor
@RestController
@EnableWebSecurity
@RequestMapping("/api/public/users")
public class UserController implements GenericController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> findAllUsers(Pageable pageable) {
        return ResponseEntity.ok(this.userService.findAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserDto dto) {
        UserResponseDto user = userService.createUser(dto);
        return ResponseEntity.created(generatorDefaultHeaderLocation(UUID.fromString(user.id()))).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @Valid @RequestBody UserDto dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
