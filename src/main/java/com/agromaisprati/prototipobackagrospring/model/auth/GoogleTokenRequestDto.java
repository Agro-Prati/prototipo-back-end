package com.agromaisprati.prototipobackagrospring.model.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para receber o token do Google Sign-In do frontend
 */
public record GoogleTokenRequestDto(
    @NotBlank(message = "Token do Google é obrigatório")
    String credential  // ID Token do Google (JWT)
) {}
