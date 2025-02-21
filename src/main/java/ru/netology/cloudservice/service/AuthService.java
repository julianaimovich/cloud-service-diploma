package ru.netology.cloudservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.exception.BadCredentialsException;
import ru.netology.cloudservice.utils.Constants.CommonConstants;

import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

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
            logger.info("User with login '{}' was successfully authenticated", userDto.getLogin());
            return new UserDto(authToken);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException();
        }
    }
}