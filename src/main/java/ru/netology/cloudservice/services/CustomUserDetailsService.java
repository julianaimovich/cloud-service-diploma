package ru.netology.cloudservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ru.netology.cloudservice.db.entities.UsersEntity;
import ru.netology.cloudservice.db.repositories.AuthoritiesRepository;
import ru.netology.cloudservice.db.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final AuthoritiesRepository authoritiesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UsersEntity user = usersRepository.findByLogin(username).orElseThrow(()
                -> new UsernameNotFoundException(username));
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UsersEntity(user.getUsername(), user.getPassword());
    }

    public List<GrantedAuthority> getAuthorities(String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authoritiesRepository.findByLogin(username).ifPresent(authoritiesEntity ->
                authorities.add(new SimpleGrantedAuthority(authoritiesEntity.getAuthority())));
        return authorities;
    }
}