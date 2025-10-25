package com.agromaisprati.prototipobackagrospring.service.auth.impl;

import com.agromaisprati.prototipobackagrospring.model.auth.LoginRequestDto;
import com.agromaisprati.prototipobackagrospring.model.auth.TokenResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.AuthProvider;
import com.agromaisprati.prototipobackagrospring.model.user.TipoUsuario;
import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.repository.user.UserRepository;
import com.agromaisprati.prototipobackagrospring.service.auth.AuthService;
import com.agromaisprati.prototipobackagrospring.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponseDto register(UserDto dto) {
        // Validar se email já existe
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setType(dto.getType());
        user.setAuthProvider(AuthProvider.LOCAL);  // Registro tradicional
        user.setEmailVerified(false);  // Email não verificado por padrão
        user.setPhone(dto.getPhone());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setDescription(dto.getDescription());
        
        User savedUser = userRepository.save(user);
        
        // Gerar token JWT para o usuário recém-criado
        String token = jwtService.generateToken(savedUser);
        
        return new TokenResponseDto(token);
    }

    @Override
    public TokenResponseDto login(LoginRequestDto login) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(login.email(), login.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new TokenResponseDto(token);
    }

    @Override
    public TokenResponseDto processOAuth2Login(String email, String name, String googleId, String picture, Boolean emailVerified) {
        // Buscar usuário existente por Google ID ou email
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            // Se não existir, criar novo usuário com dados do Google
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setGoogleId(googleId);
            newUser.setProfilePictureUrl(picture);
            newUser.setEmailVerified(emailVerified != null ? emailVerified : false);
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            newUser.setPassword(null);  // Usuário OAuth2 não tem senha local
            newUser.setType(TipoUsuario.AGRICULTOR); // Tipo padrão para OAuth2
            return userRepository.save(newUser);
        });
        
        // Se usuário já existe mas não tem Google ID, atualizar
        if (user.getGoogleId() == null && googleId != null) {
            user.setGoogleId(googleId);
            user.setProfilePictureUrl(picture);
            user.setEmailVerified(emailVerified != null ? emailVerified : false);
            user.setAuthProvider(AuthProvider.GOOGLE);
            userRepository.save(user);
        }

        // Gerar token JWT
        String token = jwtService.generateToken(user);
        
        return new TokenResponseDto(token);
    }
}
