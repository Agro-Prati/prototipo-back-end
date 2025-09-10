package com.agromaisprati.prototipobackagrospring.factories;

import java.util.UUID;

import com.agromaisprati.prototipobackagrospring.model.role.Role;

public class RoleFactory {

    public static Role createRole() {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("ROLE_USER");
        return role;
    }

}
