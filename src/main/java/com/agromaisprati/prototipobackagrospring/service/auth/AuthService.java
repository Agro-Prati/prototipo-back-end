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

    /**
     * Processa login OAuth2 do Google
     * @param email Email do usuário do Google
     * @param name Nome do usuário do Google
     * @param googleId ID único do Google
     * @param picture URL da foto de perfil
     * @param emailVerified Se o email foi verificado pelo Google
     * @return Token JWT
     */
    TokenResponseDto processOAuth2Login(String email, String name, String googleId, String picture, Boolean emailVerified);

    /**
     * Obtém dados do usuário a partir do JWT token
     * @param jwt Token JWT
     * @return Dados do usuário
     */
    UserDto getUserFromToken(String jwt);

    /**
     * Processa login com Google usando ID Token do frontend
     * @param idToken Token JWT do Google Sign-In
     * @return Token JWT do backend
     */
    TokenResponseDto loginWithGoogleToken(String idToken);
}
