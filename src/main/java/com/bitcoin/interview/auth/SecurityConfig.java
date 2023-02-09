package com.bitcoin.interview.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private final ApiKeyAuthFilter apiKeyAuthFilter;
    private final ApiKeyAuthProvider apiKeyAuthProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(apiKeyAuthFilter, AnonymousAuthenticationFilter.class)
                .authenticationProvider(apiKeyAuthProvider)
                .antMatcher("/api/v1/managements/**")
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
