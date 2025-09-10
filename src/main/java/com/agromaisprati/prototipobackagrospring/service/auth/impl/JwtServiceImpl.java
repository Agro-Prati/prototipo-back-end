package com.agromaisprati.prototipobackagrospring.service.auth.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.agromaisprati.prototipobackagrospring.controller.exceptions.UnauthorizedException;
import com.agromaisprati.prototipobackagrospring.service.auth.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${jwt.access-token.duration}")
    private int accessTokenDuration;
    @Value("${jwt.refresh-token.duration}")
    private int refreshTokenDuration;
    private String issuer = "prototipo-back-agro-spring";

    @Override
    public Jwt encodeAccessToken(Authentication authentication) {
        String accessTokenId = UUID.randomUUID().toString();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(issuer)
            .id(accessTokenId)
            .subject(authentication.getName())
            .claim("username", authentication.getName())
            .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(Duration.ofDays(accessTokenDuration)))
            .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims));
    }

    @Override
    public Jwt encodeRefreshToken(Authentication authentication) {
        String refreshTokenId = UUID.randomUUID().toString();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(issuer)
            .id(refreshTokenId)
            .subject(authentication.getName())
            .claim("username", authentication.getName())
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(Duration.ofDays(refreshTokenDuration)))
            .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims));
    }

    @Override
    public Jwt decodeJwt(String token) {
        try {
            return this.jwtDecoder.decode(token);
        }
        catch (Exception e) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    

}
