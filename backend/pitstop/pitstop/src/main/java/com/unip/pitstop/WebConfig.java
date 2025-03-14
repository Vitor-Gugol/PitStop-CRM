package com.unip.pitstop;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite todas as rotas do seu back-end serem acessadas
                .allowedOrigins("http://127.0.0.1:5500", "http://localhost:5500") // URLs da sua interface
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos suportados
                .allowedHeaders("*") // Permite qualquer header
                .allowCredentials(true); // Permite envio de credenciais, se necessário
    }
}
