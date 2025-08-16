package com.agromaisprati.prototipobackagrospring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // -> sem CSRF desabilitado, faz com que softwares externos (postman) não acesse os endpoints
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().permitAll(); // -> por enquanto permitAll para testar endpoints com mais facilidade
                })
                .build();
    }

    @Bean // PasswordEncoder por enquanto aqui, mas é necessário que fique no security filter chain principal da aplicação, utilizando anotação order(1)
    public PasswordEncoder passwordEncoderCustom() {
        return new BCryptPasswordEncoder(10);
    }

}
