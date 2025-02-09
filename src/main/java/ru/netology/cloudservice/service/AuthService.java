package ru.netology.cloudservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.cloudservice.config.Constants;
import ru.netology.cloudservice.config.Constants.CommonConstants;
import ru.netology.cloudservice.dto.UserDto;

import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public UserDto authenticate(UserDto userDto, HttpServletRequest request) {
        try {
            List<GrantedAuthority> authorities = userDetailsService.getAuthorities(userDto.getLogin());
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(
                    userDto.getLogin(), userDto.getPassword(), authorities);
            Authentication auth = authenticationManager.authenticate(authReq);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = request.getSession(true);
            String authToken = UUID.randomUUID().toString();
            session.setAttribute(CommonConstants.SPRING_SECURITY_CONTEXT, sc);
            session.setAttribute(CommonConstants.AUTH_TOKEN, authToken);
            return new UserDto(authToken);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.ErrorMessages.BAD_CREDENTIALS);
        }
    }
}