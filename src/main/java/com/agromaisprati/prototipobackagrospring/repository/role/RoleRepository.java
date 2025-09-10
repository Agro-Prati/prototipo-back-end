package com.agromaisprati.prototipobackagrospring.repository.role;

import com.agromaisprati.prototipobackagrospring.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findByName(String name);

}
