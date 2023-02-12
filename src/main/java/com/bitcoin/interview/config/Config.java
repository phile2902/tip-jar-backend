package com.bitcoin.interview.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class Config {
    //The config is to enable auto set date to createdAt and updatedAt field when the entity is saved
    @Bean
    public CustomAuditorAware auditorProvider() {
        return new CustomAuditorAware();
    }
}
