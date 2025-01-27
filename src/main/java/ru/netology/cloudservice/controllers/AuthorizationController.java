package ru.netology.cloudservice.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudservice.entities.Users;
import ru.netology.cloudservice.services.AuthorizationService;

import java.util.List;

@RestController
public class AuthorizationController {
    private final AuthorizationService service;

    public AuthorizationController(AuthorizationService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public String login(String login, String password) {
        Users user = Users.builder().login(login).password(password).build();
        List<Users> allUsers = service.getAllUsers();
        if (allUsers.contains(user)) {
            return "kek";
        }
        return "not";
    }
}