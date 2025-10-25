package com.agromaisprati.prototipobackagrospring.model.user;

import java.time.LocalDateTime;

/**
 * DTO de resposta para usuários
 */
public record UserResponseDto(
    String id, 
    String name, 
    String email, 
    TipoUsuario type, 
    String city, 
    String state, 
    String description,
    String phone,
    AuthProvider authProvider,
    String googleId,
    String profilePictureUrl,
    Boolean emailVerified,
    LocalDateTime createdAt
) {

    public UserResponseDto(User user) {
        this(
            user.getId().toString(),
            user.getName(),
            user.getEmail(),
            user.getType(),
            user.getCity(),
            user.getState(),
            user.getDescription(),
            user.getPhone(),
            user.getAuthProvider(),
            user.getGoogleId(),
            user.getProfilePictureUrl(),
            user.getEmailVerified(),
            user.getCreatedAt()
        );
    }
}
