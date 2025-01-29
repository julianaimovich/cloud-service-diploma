package ru.netology.cloudservice.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ru.netology.cloudservice.constants.HeaderValues;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InitialAuthenticationFilter implements Filter {

    public static String AUTH_TOKEN;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getHeader(HeaderValues.AUTH_TOKEN_HEADER) == null) {
            try {
                AUTH_TOKEN = UUID.randomUUID().toString();
                response.setHeader(HeaderValues.AUTH_TOKEN_HEADER, String.join(" ", HeaderValues.TOKEN_BEARER, HeaderValues.AUTH_TOKEN_HEADER));
            } catch (BadCredentialsException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        filterChain.doFilter(request, response);
    }
}