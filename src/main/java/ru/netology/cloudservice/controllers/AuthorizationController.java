package ru.netology.cloudservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudservice.constants.Endpoints;
import ru.netology.cloudservice.schemaBuilders.ErrorSchemaBuilder;
import ru.netology.cloudservice.schemas.BaseSchema;
import ru.netology.cloudservice.schemas.LoginSchema;
import ru.netology.cloudservice.services.CustomUserDetailsService;

import java.util.UUID;

@RestController
public class AuthorizationController {
    private final CustomUserDetailsService userDetailsService;

    public AuthorizationController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(Endpoints.LOGIN_URL)
    public ResponseEntity<BaseSchema> login(@RequestBody LoginSchema request) {
        UserDetails user = userDetailsService.loadUserByUsername(request.getLogin());
        if (user != null && user.getPassword().equals(request.getPassword())) {
            LoginSchema response = new LoginSchema(UUID.randomUUID().toString());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorSchemaBuilder.badCredentialsError());
        }
    }

    @GetMapping(Endpoints.LOGIN_URL)
    public String login() {
        return "login";
    }
}