package com.agromaisprati.prototipobackagrospring.service.auth;

import com.agromaisprati.prototipobackagrospring.model.auth.LoginRequestDto;
import com.agromaisprati.prototipobackagrospring.model.auth.TokenResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;

/**
 * Interface simplificada para operações de autenticação
 */
public interface AuthService {
    /**
     * Registra um novo usuário e retorna token JWT
     */
    TokenResponseDto register(UserDto user);

    /**
     * Realiza login e retorna JWT token
     */
    TokenResponseDto login(LoginRequestDto login);
}
