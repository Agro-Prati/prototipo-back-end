package com.agromaisprati.prototipobackagrospring.service.auth;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * Interface simplificada para operações JWT
 */
public interface JwtService {

    /**
     * Gera um token JWT para o usuário
     */
    String generateToken(UserDetails userDetails);

    /**
     * Extrai o username do token
     */
    String extractUsername(String token);

    /**
     * Extrai a data de expiração do token
     */
    Date extractExpiration(String token);

    /**
     * Valida o token
     */
    Boolean validateToken(String token, UserDetails userDetails);

}
