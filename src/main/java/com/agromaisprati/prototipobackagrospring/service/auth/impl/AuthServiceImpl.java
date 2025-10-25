package com.agromaisprati.prototipobackagrospring.service.auth.impl;

import com.agromaisprati.prototipobackagrospring.model.auth.LoginRequestDto;
import com.agromaisprati.prototipobackagrospring.model.auth.TokenResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.AuthProvider;
import com.agromaisprati.prototipobackagrospring.model.user.TipoUsuario;
import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.repository.user.UserRepository;
import com.agromaisprati.prototipobackagrospring.service.auth.AuthService;
import com.agromaisprati.prototipobackagrospring.service.auth.GoogleTokenVerifier;
import com.agromaisprati.prototipobackagrospring.service.auth.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final GoogleTokenVerifier googleTokenVerifier;

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
        
        // Converter User para UserResponseDto para retornar
        UserResponseDto userResponseDto = new UserResponseDto(savedUser);
        
        return new TokenResponseDto(token, userResponseDto);
    }

    @Override
    public TokenResponseDto login(LoginRequestDto login) {
        // Autenticar usuário
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(login.email(), login.password())
        );

        User user = userRepository.findByEmail(login.email())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        String token = jwtService.generateToken(user);
        
        // Converter User para UserResponseDto para retornar
        UserResponseDto userResponseDto = new UserResponseDto(user);

        return new TokenResponseDto(token, userResponseDto);
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
        
        // Converter User para UserResponseDto para retornar
        UserResponseDto userResponseDto = new UserResponseDto(user);
        
        return new TokenResponseDto(token, userResponseDto);
    }

    @Override
    public UserDto getUserFromToken(String jwt) {
        // Extrair email do token
        String email = jwtService.extractUsername(jwt);
        
        // Buscar usuário no banco
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Converter para DTO e retornar (sem senha)
        UserDto dto = new UserDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(null);  // Não retornar senha
        dto.setType(user.getType());
        dto.setPhone(user.getPhone());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setDescription(user.getDescription());
        
        return dto;
    }

    @Override
    public TokenResponseDto loginWithGoogleToken(String idToken) {
        // Validar token do Google
        GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(idToken);
        
        // Extrair dados do usuário
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String googleId = payload.getSubject();
        String picture = (String) payload.get("picture");
        Boolean emailVerified = payload.getEmailVerified();
        
        // Buscar ou criar usuário
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setGoogleId(googleId);
            newUser.setProfilePictureUrl(picture);
            newUser.setEmailVerified(emailVerified != null ? emailVerified : false);
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            newUser.setPassword(null);
            newUser.setType(TipoUsuario.AGRICULTOR);
            return userRepository.save(newUser);
        });
        
        // Atualizar dados do Google se necessário
        if (user.getGoogleId() == null && googleId != null) {
            user.setGoogleId(googleId);
            user.setProfilePictureUrl(picture);
            user.setEmailVerified(emailVerified != null ? emailVerified : false);
            user.setAuthProvider(AuthProvider.GOOGLE);
            userRepository.save(user);
        }
        
        // Gerar token JWT
        String token = jwtService.generateToken(user);
        UserResponseDto userResponseDto = new UserResponseDto(user);
        
        return new TokenResponseDto(token, userResponseDto);
    }
}
