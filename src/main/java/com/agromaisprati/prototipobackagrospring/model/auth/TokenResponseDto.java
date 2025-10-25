package com.agromaisprati.prototipobackagrospring.model.auth;

import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;

/**
 * DTO de resposta para autenticação
 * Contém o token JWT e os dados do usuário logado
 */
public record TokenResponseDto(
    String accessToken,
    UserResponseDto user
) {
    // Construtor para compatibilidade com código legado (apenas token)
    public TokenResponseDto(String accessToken) {
        this(accessToken, null);
    }
}

