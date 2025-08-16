package com.agromaisprati.prototipobackagrospring.validator.role;

import com.agromaisprati.prototipobackagrospring.controller.exceptions.NotFoundException;
import com.agromaisprati.prototipobackagrospring.model.role.Role;
import com.agromaisprati.prototipobackagrospring.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleValidator {

    private final RoleRepository roleRepository;

    public Role findByRoleId(UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ROLE"));
    }

}
