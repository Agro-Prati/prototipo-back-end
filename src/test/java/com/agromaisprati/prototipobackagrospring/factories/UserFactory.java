package com.agromaisprati.prototipobackagrospring.factories;

import java.util.UUID;

import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;

public class UserFactory {

    public static User createUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setRole(RoleFactory.createRole());
        return user;
    }

    public static UserDto createUserDto() {
        UserDto dto = new UserDto();
        dto.setName("John Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPassword("password");
        return dto;
    }

}
