package ru.netology.cloudservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto, HttpServletRequest request) {
        UserDto user = authService.authenticate(userDto, request);
        return ResponseEntity.ok(user);
    }

    @GetMapping(Endpoints.LOGIN)
    public String login() {
        return "login";
    }
}