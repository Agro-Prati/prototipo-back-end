package com.agromaisprati.prototipobackagrospring.model.user;

/**
 * DTO de resposta para usuários
 */
public record UserResponseDto(String id, String name, String email, TipoUsuario type, String city, String state) {

    public UserResponseDto(User user) {
        this(
            user.getId().toString(),
            user.getName(),
            user.getEmail(),
            user.getType(),
            user.getCity(),
            user.getState()
        );
    }
}
