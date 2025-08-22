package com.agromaisprati.prototipobackagrospring.service.auth;

import org.springframework.security.oauth2.jwt.Jwt;

public interface BlackListTokenService {

    void addTokenToBlackList(Jwt accessToken);
    boolean isTokenBlackListed(String accessTokenId);

}
