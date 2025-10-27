package com.agromaisprati.prototipobackagrospring.repository.user;

import com.agromaisprati.prototipobackagrospring.model.user.TipoUsuario;
import com.agromaisprati.prototipobackagrospring.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByType(TipoUsuario type, Pageable pageable);
}

