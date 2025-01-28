package ru.netology.cloudservice.controllers;

import org.springframework.web.bind.annotation.*;
import ru.netology.cloudservice.entities.Users;
import ru.netology.cloudservice.schemaBuilders.ErrorSchemaBuilder;
import ru.netology.cloudservice.schemas.LoginRequestSchema;
import ru.netology.cloudservice.schemas.LoginResponseSchema;
import ru.netology.cloudservice.schemas.ResponseSchema;
import ru.netology.cloudservice.services.UserService;

import java.util.Optional;
import java.util.UUID;

@RestController
public class AuthorizationController {
    private final UserService userService;

    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseSchema login(@RequestBody LoginRequestSchema schema) {
        Optional<Users> user = userService.findUserByLoginAndPassword(schema.getLogin(), schema.getPassword());
        if (user.isEmpty()) {
            return ErrorSchemaBuilder.badCredentialsError();
        }
        return new LoginResponseSchema(UUID.randomUUID().toString());
    }
}