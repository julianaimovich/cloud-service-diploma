package ru.netology.cloudservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.cloudservice.repository.AuthoritiesRepository;
import ru.netology.cloudservice.repository.UsersRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    private final AuthoritiesRepository authoritiesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByLogin(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(username));
    }

    public List<GrantedAuthority> getAuthorities(String username) {
        return authoritiesRepository.findByLogin(username)
                .map(authoritiesEntity ->
                        List.of((GrantedAuthority) new SimpleGrantedAuthority(authoritiesEntity.getAuthority())))
                .orElseThrow(() ->
                        new UsernameNotFoundException(username));
    }
}