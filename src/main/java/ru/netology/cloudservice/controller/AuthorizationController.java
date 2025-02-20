package ru.netology.cloudservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.exception.InvalidSessionTokenException;
import ru.netology.cloudservice.exception.SessionNotFoundException;
import ru.netology.cloudservice.service.AuthService;
import ru.netology.cloudservice.utils.Constants.CommonConstants;
import ru.netology.cloudservice.utils.Constants.Endpoints;

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

    @PostMapping(Endpoints.LOGOUT)
    public ResponseEntity<?> logout(@RequestHeader(CommonConstants.AUTH_TOKEN) String authToken,
                                    HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException();
        }

        String sessionToken = (String) session.getAttribute(CommonConstants.AUTH_TOKEN);

        if (!authToken.equals(sessionToken)) {
            throw new InvalidSessionTokenException();
        }

        session.invalidate();
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().build();
    }
}