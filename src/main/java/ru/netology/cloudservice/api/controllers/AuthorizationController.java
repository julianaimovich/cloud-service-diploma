package ru.netology.cloudservice.api.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudservice.api.schemas.UserSchema;
import ru.netology.cloudservice.constants.Endpoints;
import ru.netology.cloudservice.services.CustomUserDetailsService;

import java.util.List;

@RestController
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthorizationController(CustomUserDetailsService userDetailsService,
                                   AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    /*@PostMapping(Endpoints.LOGIN)
    public ResponseEntity<BaseSchema> login(@RequestBody final UserSchema userSchema) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userSchema.getLogin());
        if (userDetails != null && userDetails.getPassword().equals(userSchema.getPassword())) {
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken
                    (userSchema.getLogin(), userSchema.getPassword());
            Authentication auth = authenticationManager.authenticate(authReq);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            UserSchema user = new UserSchema(UUID.randomUUID().toString());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorSchemaBuilder.badCredentialsError());
        }
    }*/

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody UserSchema userSchema, final HttpServletRequest request) {
        List<GrantedAuthority> authorities = userDetailsService.getAuthorities(userSchema.getLogin());
        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(userSchema.getLogin(), userSchema.getPassword(), authorities);
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
    }

    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public void printUser() {
        SecurityContext sc = SecurityContextHolder.getContext();
        System.out.println("Logged User: "+sc.getAuthentication().getName());
    }

    @GetMapping(Endpoints.LOGIN)
    public String login() {
        return "login";
    }
}