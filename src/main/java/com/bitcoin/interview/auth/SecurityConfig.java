package com.bitcoin.interview.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ApiKeyAuthenticationFilter apiAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/v1/managements/*")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(apiAuthenticationFilter, BasicAuthenticationFilter.class)
                .authorizeRequests().anyRequest().authenticated();
    }
}
