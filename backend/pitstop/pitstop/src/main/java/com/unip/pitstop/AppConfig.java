package com.unip.pitstop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public HttpMessageConverter<?> customJsonMessageConverter() {
        // Log informando o registro do MessageConverter
        System.out.println("Registrando HttpMessageConverter com suporte a charset=UTF-8...");

        // Instanciando o converter Jackson
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // Adicionando os MediaTypes suportados
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON); // application/json
        supportedMediaTypes.add(MediaType.valueOf("application/json;charset=UTF-8")); // application/json;charset=UTF-8
        converter.setSupportedMediaTypes(supportedMediaTypes);

        return converter;
    }
}
