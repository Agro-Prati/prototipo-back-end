package com.agromaisprati.prototipobackagrospring.model.user;

public record UserResponseDto(String id, String name, String email) {

    public UserResponseDto(User user) {
        this(user.getId().toString(), user.getName(), user.getEmail());
    }

}
