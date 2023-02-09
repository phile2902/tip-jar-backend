package com.bitcoin.interview.auth;

import com.bitcoin.interview.model.User;
import com.bitcoin.interview.service.IAuthService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private final IAuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String apiKey = request.getHeader("apiKey");
        if (apiKey != null) {
            User user = null;
            try {
                user = authService.findUserByApiKey(apiKey);
            } catch (Exception ex) {
                Logger.getLogger(ApiKeyAuthFilter.class.getName()).log(Level.SEVERE, "Failed to find user by api key", ex);
            }
            
            if (user != null) {
                Authentication authentication = new PreAuthenticatedAuthenticationToken(user, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
    
}
