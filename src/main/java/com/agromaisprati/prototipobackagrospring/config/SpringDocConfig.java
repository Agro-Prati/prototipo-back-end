package com.agromaisprati.prototipobackagrospring.config;

import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

/**
 * Configuração do SpringDoc para corrigir problema com Pageable no Swagger UI
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public ParameterCustomizer parameterCustomizer() {
        return (parameterModel, methodParameter) -> {
            if (methodParameter.getParameterType().equals(Pageable.class)) {
                // Remove o schema padrão problemático do Pageable
                parameterModel.setSchema(null);
                parameterModel.setContent(null);
            }
            return parameterModel;
        };
    }
}
