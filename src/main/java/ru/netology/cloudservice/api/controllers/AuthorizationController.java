package ru.netology.cloudservice.api.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.cloudservice.api.schemas.BaseSchema;
import ru.netology.cloudservice.api.schemas.UserSchema;
import ru.netology.cloudservice.constants.Endpoints;
import ru.netology.cloudservice.constants.ErrorDescriptions;
import ru.netology.cloudservice.constants.RequestParamValues;
import ru.netology.cloudservice.services.CustomUserDetailsService;

import java.util.List;
import java.util.UUID;

@RestController
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthorizationController(CustomUserDetailsService userDetailsService,
                                   AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(Endpoints.LOGIN)
    public ResponseEntity<BaseSchema> login(@RequestBody UserSchema userSchema, final HttpServletRequest request) {
        List<GrantedAuthority> authorities = userDetailsService.getAuthorities(userSchema.getLogin());
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken
                (userSchema.getLogin(), userSchema.getPassword(), authorities);
        Authentication auth = authenticationManager.authenticate(authReq);
        if (auth.isAuthenticated()) {
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute(RequestParamValues.SPRING_SECURITY_CONTEXT, sc);
            UserSchema user = new UserSchema(UUID.randomUUID().toString());
            return ResponseEntity.ok(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorDescriptions.BAD_CREDENTIALS);
        }
    }

    @GetMapping(Endpoints.LOGIN)
    public String login() {
        return "login";
    }
}