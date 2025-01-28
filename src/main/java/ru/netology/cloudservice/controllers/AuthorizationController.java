package ru.netology.cloudservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudservice.entities.Users;
import ru.netology.cloudservice.schemaBuilders.ErrorSchemaBuilder;
import ru.netology.cloudservice.schemas.*;
import ru.netology.cloudservice.services.UsersService;

import java.util.Optional;
import java.util.UUID;

@RestController
public class AuthorizationController {
    private final UsersService usersService;

    public AuthorizationController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseSchema> login(@RequestBody LoginRequestSchema schema) {
        Optional<Users> user = usersService.findUserByLoginAndPassword(schema.getLogin(), schema.getPassword());
        if (user.isEmpty()) {
            return new ResponseEntity<>(ErrorSchemaBuilder.badCredentialsError(), HttpStatus.BAD_REQUEST);
        }
        LoginResponseSchema responseSchema = new LoginResponseSchema(UUID.randomUUID().toString());
        return new ResponseEntity<>(responseSchema, HttpStatus.OK);
    }
}