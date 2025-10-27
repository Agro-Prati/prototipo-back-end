package com.agromaisprati.prototipobackagrospring.security;

import com.agromaisprati.prototipobackagrospring.model.user.AuthProvider;
import com.agromaisprati.prototipobackagrospring.model.user.TipoUsuario;
import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.repository.user.UserRepository;
import com.agromaisprati.prototipobackagrospring.service.auth.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * Handler customizado para sucesso de autenticação OAuth2
 * Gera JWT e armazena em HTTP-only cookie seguro
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    
    @Value("${FRONTEND_URL:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        // Extrair dados do Google
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String googleId = oauth2User.getAttribute("sub");
        String picture = oauth2User.getAttribute("picture");
        Boolean emailVerified = oauth2User.getAttribute("email_verified");
        
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
        
        // Atualizar dados se usuário já existe
        if (user.getGoogleId() == null && googleId != null) {
            user.setGoogleId(googleId);
            user.setProfilePictureUrl(picture);
            user.setEmailVerified(emailVerified != null ? emailVerified : false);
            if (user.getAuthProvider() == null) {
                user.setAuthProvider(AuthProvider.GOOGLE);
            }
            userRepository.save(user);
        }
        
        // Gerar token JWT
        String token = jwtService.generateToken(user);
        
        // Criar cookie HTTP-only seguro com SameSite=Lax
        // Usando ResponseCookie para melhor compatibilidade
        String cookieValue = String.format(
            "JWT_TOKEN=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
            token,
            24 * 60 * 60 // 24 horas
        );
        response.addHeader("Set-Cookie", cookieValue);
        
        // Redirecionar para frontend
        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/callback")
                .queryParam("success", "true")
                .build().toUriString();
        
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
