package com.agromaisprati.prototipobackagrospring.model.user;

/**
 * Enum que representa o provedor de autenticação do usuário
 */
public enum AuthProvider {
    LOCAL,   // Usuário criado com email/senha tradicional
    GOOGLE   // Usuário criado via Google OAuth2
}
