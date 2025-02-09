package ru.netology.cloudservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.cloudservice.config.Constants.Endpoints;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.service.AuthService;

@RestController
public class AuthorizationController {

    private final AuthService authService;

    public AuthorizationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(Endpoints.LOGIN)
    public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletRequest request) {
        try {
            UserDto user = authService.authenticate(userDto, request);
            return ResponseEntity.ok(user);
        } catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}