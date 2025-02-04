package ru.netology.cloudservice.controller;

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
import ru.netology.cloudservice.constants.*;
import ru.netology.cloudservice.dto.UserDto;
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
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto, final HttpServletRequest request) {
        List<GrantedAuthority> authorities = userDetailsService.getAuthorities(userDto.getLogin());
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken
                (userDto.getLogin(), userDto.getPassword(), authorities);
        Authentication auth = authenticationManager.authenticate(authReq);
        if (auth.isAuthenticated()) {
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute(CommonConstants.SPRING_SECURITY_CONTEXT, sc);
            UserDto user = new UserDto(UUID.randomUUID().toString());
            return ResponseEntity.ok(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.BAD_CREDENTIALS);
        }
    }

    @GetMapping(Endpoints.LOGIN)
    public String login() {
        return "login";
    }
}