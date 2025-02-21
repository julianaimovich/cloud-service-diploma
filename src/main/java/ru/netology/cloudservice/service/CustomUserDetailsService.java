package ru.netology.cloudservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.netology.cloudservice.exception.UserNotFoundException;
import ru.netology.cloudservice.repository.AuthoritiesRepository;
import ru.netology.cloudservice.repository.UsersRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    private final AuthoritiesRepository authoritiesRepository;

    private static final Logger logger = LogManager.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.info("Fetching user from DB with login: {}", username);
        return usersRepository.findByLogin(username)
                .orElseThrow(() ->
                        new UserNotFoundException(username));
    }

    public List<GrantedAuthority> getAuthorities(String username) {
        logger.info("Fetching authorities from DB by user login: {}", username);
        return authoritiesRepository.findByLogin(username)
                .map(authoritiesEntity ->
                        List.of((GrantedAuthority) new SimpleGrantedAuthority(authoritiesEntity.getAuthority())))
                .orElseThrow(() ->
                        new UserNotFoundException(username));
    }
}