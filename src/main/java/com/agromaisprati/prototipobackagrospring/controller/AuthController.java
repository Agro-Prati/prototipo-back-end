package com.agromaisprati.prototipobackagrospring.controller;

import com.agromaisprati.prototipobackagrospring.model.auth.LoginRequestDto;
import com.agromaisprati.prototipobackagrospring.model.auth.TokenResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de autenticação - Endpoints públicos para registro e login
 */
@Tag(name = "Autenticação", description = "Endpoints públicos para registro e login de usuários")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registrar novo usuário", 
               description = "Cria uma nova conta e retorna o token JWT para autenticação imediata")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso - Token JWT retornado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos - Verifique o formato dos campos"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@Valid @RequestBody UserDto dto) {
        TokenResponseDto response = authService.register(dto);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Fazer login", 
               description = "Autentica um usuário existente e retorna token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso - Token JWT retornado"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas - Email ou senha incorretos")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto login) {
        return ResponseEntity.ok(authService.login(login));
    }
}
