package com.agromaisprati.prototipobackagrospring.service.user;

import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.validator.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    @Override
    public User registerUser(User user) {
        userValidator.hasEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }
}
