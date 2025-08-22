package com.agromaisprati.prototipobackagrospring.model.user;

import com.agromaisprati.prototipobackagrospring.model.role.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Table(name = "tb_user")
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(Arrays.asList(this.role));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
