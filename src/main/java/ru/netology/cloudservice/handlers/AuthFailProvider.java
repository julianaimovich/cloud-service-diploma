package ru.netology.cloudservice.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import ru.netology.cloudservice.schemaBuilders.ErrorSchemaBuilder;

import static ru.netology.cloudservice.schemas.ResponseSchema.write;

public class AuthFailProvider implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exc) {
        if (exc instanceof BadCredentialsException) {
            write(response, ErrorSchemaBuilder.badCredentialsError(), HttpStatus.BAD_REQUEST);
        } else {
            write(response, ErrorSchemaBuilder.internalServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}