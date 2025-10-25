package com.agromaisprati.prototipobackagrospring.controller;

import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserUpdateDto;
import com.agromaisprati.prototipobackagrospring.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para gerenciamento administrativo de usuários
 * Endpoints protegidos que requerem autenticação JWT e permissões administrativas
 * Para operações do próprio usuário (perfil), utilize o ProfileController
 */
@Tag(name = "Usuários (Admin)", description = "Operações administrativas de gerenciamento de usuários - requer permissões de administrador")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController implements GenericController {

    private final UserService userService;

    @Operation(summary = "Listar todos os usuários (Admin)", 
               description = "Retorna lista paginada de todos os usuários cadastrados. Requer permissões administrativas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado - Token inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Proibido - Sem permissões administrativas")
    })
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> findAllUsers(Pageable pageable) {
        return ResponseEntity.ok(this.userService.findAllUsers(pageable));
    }

    @Operation(summary = "Buscar usuário por ID (Admin)", 
               description = "Retorna os dados de um usuário específico pelo ID. Requer permissões administrativas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Proibido - Sem permissões administrativas")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @Operation(summary = "Criar novo usuário (Admin)", 
               description = "Cria um novo usuário no sistema. Requer permissões administrativas.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Proibido - Sem permissões administrativas")
    })
    @PostMapping
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserDto dto) {
        UserResponseDto user = userService.createUser(dto);
        return ResponseEntity.created(generatorDefaultHeaderLocation(UUID.fromString(user.id()))).build();
    }

    @Operation(summary = "Atualizar usuário completo (Admin)", 
               description = "Atualiza todos os dados de um usuário existente. Requer permissões administrativas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Proibido - Sem permissões administrativas")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @Valid @RequestBody UserDto dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Atualizar usuário parcialmente (Admin)", 
               description = "Atualiza apenas os campos fornecidos de um usuário existente. Requer permissões administrativas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Proibido - Sem permissões administrativas")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUserPartial(@PathVariable String id, @Valid @RequestBody UserUpdateDto dto) {
        userService.updateUserPartial(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deletar usuário (Admin)", 
               description = "Remove um usuário do sistema. Requer permissões administrativas.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Proibido - Sem permissões administrativas")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
