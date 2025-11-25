package com.example.penny_juice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        logger.info("Autenticación exitosa para principal='{}'. Authorities={}", authentication.getName(), authorities);

        boolean isAdmin = authorities.stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (isAdmin) {
            logger.info("Usuario '{}' tiene ROLE_ADMIN — redirigiendo a /panel", authentication.getName());
            response.sendRedirect(request.getContextPath() + "/panel");
        } else {
            logger.info("Usuario '{}' no es ADMIN — redirigiendo a /", authentication.getName());
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
