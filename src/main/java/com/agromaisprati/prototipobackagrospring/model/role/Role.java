package com.agromaisprati.prototipobackagrospring.model.role;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = "tb_role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

}
