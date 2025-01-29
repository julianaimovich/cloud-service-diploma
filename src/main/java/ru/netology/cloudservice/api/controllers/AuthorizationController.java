package ru.netology.cloudservice.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudservice.api.schemaBuilders.ErrorSchemaBuilder;
import ru.netology.cloudservice.api.schemas.BaseSchema;
import ru.netology.cloudservice.api.schemas.UserSchema;
import ru.netology.cloudservice.config.InitialAuthenticationFilter;
import ru.netology.cloudservice.constants.Endpoints;
import ru.netology.cloudservice.services.CustomUserDetailsService;

@RestController
public class AuthorizationController {

    private final CustomUserDetailsService userDetailsService;

    public AuthorizationController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(Endpoints.LOGIN)
    public ResponseEntity<BaseSchema> login(@RequestBody UserSchema request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());
        if (userDetails != null && userDetails.getPassword().equals(request.getPassword())) {
            UserSchema user = new UserSchema(InitialAuthenticationFilter.AUTH_TOKEN);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorSchemaBuilder.badCredentialsError());
        }
    }

    @GetMapping(Endpoints.LOGIN)
    public String login() {
        return "login";
    }
}