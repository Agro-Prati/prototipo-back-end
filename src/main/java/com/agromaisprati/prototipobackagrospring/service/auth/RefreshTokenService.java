package com.agromaisprati.prototipobackagrospring.service.auth;

import org.springframework.security.oauth2.jwt.Jwt;

public interface RefreshTokenService {

    void saveRefreshToken(Jwt refreshToken);
    void deleteRefreshToken(String refreshTokenId);
    boolean refreshTokenExists(String refreshTokenId);

}
