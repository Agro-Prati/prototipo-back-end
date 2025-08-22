package com.agromaisprati.prototipobackagrospring.security;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import com.agromaisprati.prototipobackagrospring.service.auth.impl.BlackListTokenServiceImpl;
import com.agromaisprati.prototipobackagrospring.service.auth.impl.JwtServiceImpl;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
/**
 * Classe de configuração de segurança da aplicação, responsável por definir as regras de acesso
 * e os componentes relacionados à autenticação e autorização.
 *
 * Anotações utilizadas:
 * - @Configuration: indica que esta classe contém beans de configuração.
 * - @EnableWebSecurity: ativa a configuração de segurança da web.
 * - @EnableMethodSecurity: permite o uso de anotações como @PreAuthorize nos métodos dos controllers.
 *
 * Principais componentes:
 * - SecurityFilterChain: define a cadeia de filtros de segurança. Aqui, o CSRF está desabilitado
 *   para facilitar testes com ferramentas externas (como Postman), e todas as requisições estão
 *   temporariamente liberadas com `permitAll()` para facilitar o desenvolvimento inicial.
 *
 * - PasswordEncoder: fornece um encoder seguro para senhas usando BCrypt com força 10.
 *   Embora esteja definido aqui, recomenda-se movê-lo para a cadeia principal de segurança
 *   com a anotação @Order(1), garantindo que seja utilizado corretamente em cenários mais complexos.
 *
 * Essa configuração é essencial para proteger os endpoints da aplicação e deve ser ajustada
 * conforme o sistema evolui para ambientes de produção.
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerConfiguration {

    @Value("${jwt.secret}")
    private String secret;
    private String algorithm = "HmacSHA256";

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, JwtServiceImpl jwtService, BlackListTokenServiceImpl blackListTokenService) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // -> sem CSRF desabilitado, faz com que softwares externos (postman) não acesse os endpoints
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().permitAll(); // -> por enquanto permitAll para testar endpoints com mais facilidade
                })
                .addFilterBefore(new BlackListTokenFilter(blackListTokenService, jwtService), BearerTokenAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(this.jwtAuthenticationConverter())))
                .build();
    }

    @Bean // PasswordEncoder por enquanto aqui, mas é necessário que fique no security filter chain principal da aplicação, utilizando anotação order(1)
    public PasswordEncoder passwordEncoderCustom() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), this.algorithm);
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), this.algorithm);
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
