package com.bitcoin.interview.auth;

import com.bitcoin.interview.model.User;
import com.bitcoin.interview.service.IAuthService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter implements Filter {

    static final private String AUTH_KEY = "apiKey";
    
    private final IAuthService authService;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        if(request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            String apiKey = getApiKey((HttpServletRequest) request);
            if(apiKey != null) {
                if(isValidApiKey(apiKey)) {
                    ApiKeyAuthenticationToken apiToken = new ApiKeyAuthenticationToken(apiKey, AuthorityUtils.NO_AUTHORITIES);
                    SecurityContextHolder.getContext().setAuthentication(apiToken);
                } else {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.setStatus(401);
                    httpResponse.getWriter().write("Invalid API Key");
                    return;
                }
            }
        }
        
        chain.doFilter(request, response);
    }
    
    private Boolean isValidApiKey(String requestedApiKey) {
        User user = null;
        try {
            user = authService.findUserByApiKey(requestedApiKey);
        } catch (Exception ex) {
            Logger.getLogger(ApiKeyAuthenticationFilter.class.getName()).log(Level.SEVERE, "Fail to get user by api key", ex);
        }
        
        return user != null;
    }

    private String getApiKey(HttpServletRequest httpRequest) {
        return httpRequest.getHeader(AUTH_KEY);
    }
    
}
