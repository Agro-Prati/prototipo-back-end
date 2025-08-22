package com.agromaisprati.prototipobackagrospring.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtService {

    Jwt encodeAccessToken(Authentication authentication);
    Jwt encodeRefreshToken(Authentication authentication);
    Jwt decodeJwt(String token);

}
