package com.agromaisprati.prototipobackagrospring.factories;

import java.util.UUID;

import com.agromaisprati.prototipobackagrospring.model.user.TipoUsuario;
import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;

public class UserFactory {

    public static User createUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setType(TipoUsuario.AGRICULTOR);
        user.setCity("Porto Alegre");
        user.setState("RS");
        return user;
    }

    public static UserDto createUserDto() {
        UserDto dto = new UserDto();
        dto.setName("John Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPassword("password");
        dto.setType(TipoUsuario.AGRICULTOR);
        dto.setCity("Porto Alegre");
        dto.setState("RS");
        return dto;
    }

}
