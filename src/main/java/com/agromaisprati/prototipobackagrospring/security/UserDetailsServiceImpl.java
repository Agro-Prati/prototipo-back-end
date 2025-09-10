package com.agromaisprati.prototipobackagrospring.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.agromaisprati.prototipobackagrospring.controller.exceptions.UnauthorizedException;
import com.agromaisprati.prototipobackagrospring.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
            .orElseThrow(() -> new UnauthorizedException("Invalid Credentials"));
    }

}
