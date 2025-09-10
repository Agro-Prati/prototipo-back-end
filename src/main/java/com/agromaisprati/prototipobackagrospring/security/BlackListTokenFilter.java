package com.agromaisprati.prototipobackagrospring.security;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.agromaisprati.prototipobackagrospring.service.auth.BlackListTokenService;
import com.agromaisprati.prototipobackagrospring.service.auth.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BlackListTokenFilter extends OncePerRequestFilter {

    private final BlackListTokenService blackListTokenService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String bearerToken = authorizationHeader.split(" ")[1];
            try {
                Jwt decodedAccessToken = this.jwtService.decodeJwt(bearerToken);
                if (this.blackListTokenService.isTokenBlackListed(decodedAccessToken.getId())) {
                    this.sendUnauthorizedResponse(request, response, "Token is blacklisted");
                    return;
                }
            }
            catch (Exception e) {
                this.sendUnauthorizedResponse(request, response, "Invalid token");
            }
        }
        filterChain.doFilter(request, response);
    }

    private void sendUnauthorizedResponse(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String errorResponse = """
            {
                "timestamp": "%s",
                "status": 401,
                "error": "Unauthorized",
                "message": "%s",
                "path": "%s"
            }
        """.formatted(Instant.now().toString(), message, request.getRequestURI());
        response.getWriter().write(errorResponse);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
