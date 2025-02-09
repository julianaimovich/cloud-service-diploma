package ru.netology.cloudservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.cloudservice.config.Constants;
import ru.netology.cloudservice.config.Constants.CommonConstants;
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

    @PostMapping(Endpoints.LOGOUT)
    public ResponseEntity<?> logout(@RequestHeader(CommonConstants.AUTH_TOKEN) String authToken,
                                    HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Constants.ErrorMessages.SESSION_NOT_FOUND);
        }

        String sessionToken = (String) session.getAttribute(CommonConstants.AUTH_TOKEN);

        if (!authToken.equals(sessionToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Constants.ErrorMessages.INVALID_AUTH_TOKEN);
        }

        session.invalidate();
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().build();
    }
}