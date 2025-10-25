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
 * 
 * Para login com Google OAuth2, acesse: GET /oauth2/authorization/google
 * (esse endpoint é fornecido automaticamente pelo Spring Security)
 */
@Tag(name = "Autenticação", description = """
    Endpoints públicos para registro e login de usuários.
    
    **Login com Google OAuth2:**
    Para fazer login com Google, acesse diretamente no navegador:
    `GET /oauth2/authorization/google`
    
    Este endpoint redireciona para o Google, e após autenticação retorna um JWT.
    """)
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

    @Operation(
        summary = "⚠️ Callback OAuth2 (NÃO USAR MANUALMENTE)", 
        description = "⚠️ Este endpoint é chamado automaticamente pelo Spring Security após login com Google. " +
                      "Para fazer login com Google, use: GET /oauth2/authorization/google",
        hidden = true  // Esconde do Swagger UI
    )
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "Redireciona para frontend com JWT"),
        @ApiResponse(responseCode = "401", description = "Falha na autenticação OAuth2")
    })
    @GetMapping("/oauth2/success")
    public ResponseEntity<TokenResponseDto> oauth2Success(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String googleId,
            @RequestParam(required = false) String picture,
            @RequestParam(defaultValue = "false") Boolean emailVerified) {
        TokenResponseDto response = authService.processOAuth2Login(email, name, googleId, picture, emailVerified);
        return ResponseEntity.ok(response);
    }
}
