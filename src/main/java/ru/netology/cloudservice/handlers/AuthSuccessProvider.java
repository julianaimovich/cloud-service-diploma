package ru.netology.cloudservice.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.netology.cloudservice.schemas.LoginResponseSchema;
import ru.netology.cloudservice.schemas.ResponseSchema;

import java.util.UUID;

public class AuthSuccessProvider implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginResponseSchema authToken = new LoginResponseSchema(UUID.randomUUID().toString());
        ResponseSchema.write(response, authToken, HttpStatus.OK);
    }
}