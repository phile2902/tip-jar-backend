package com.bitcoin.interview.auth;

import com.bitcoin.interview.model.User;
import com.bitcoin.interview.service.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter implements Filter {

    static final private String AUTH_KEY = "apiKey";

    private final IAuthService authService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            String requestUri = ((HttpServletRequest) request).getRequestURL().toString();
            //Checking if the uri matches the endpoints' starter then we will apply this checking, otherwise, it will pass
            //this filter for the others
            if (requestUri.contains("/api/v1/managements")) {
                String apiKey = getApiKey((HttpServletRequest) request);
                if (apiKey != null && isValidApiKey(apiKey)) {
                    ApiKeyAuthenticationToken apiToken = new ApiKeyAuthenticationToken(apiKey, AuthorityUtils.NO_AUTHORITIES);
                    SecurityContextHolder.getContext().setAuthentication(apiToken);
                } else {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
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
