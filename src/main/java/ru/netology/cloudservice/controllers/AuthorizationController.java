package ru.netology.cloudservice.controllers;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudservice.entities.Users;
import ru.netology.cloudservice.schemaBuilders.ErrorSchemaBuilder;
import ru.netology.cloudservice.schemas.LoginRequestSchema;
import ru.netology.cloudservice.schemas.LoginResponseSchema;
import ru.netology.cloudservice.schemas.ResponseSchema;
import ru.netology.cloudservice.services.CustomUserDetailsService;

import java.util.UUID;

@RestController
public class AuthorizationController {
    private final CustomUserDetailsService userDetailsService;

    public AuthorizationController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseSchema login(@RequestBody LoginRequestSchema request) {
        UserDetails user = userDetailsService.loadUserByUsername(request.getLogin());
        if (user != null && user.getPassword().equals(request.getPassword())) {
            return new LoginResponseSchema(UUID.randomUUID().toString());
        } else {
            return ErrorSchemaBuilder.badCredentialsError();
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}