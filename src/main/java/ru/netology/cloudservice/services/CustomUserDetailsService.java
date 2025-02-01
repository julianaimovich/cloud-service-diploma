package ru.netology.cloudservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.cloudservice.db.entities.AuthoritiesEntity;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity user = usersRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username));
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UsersEntity(user.getUsername(), user.getPassword());
    }

    public List<GrantedAuthority> getAuthorities(String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        AuthoritiesEntity authoritiesEntity = authoritiesRepository.findByLogin(username).orElse(null);
        if (authoritiesEntity != null) {
            authorities.add(new SimpleGrantedAuthority(authoritiesEntity.getAuthority()));
        }
        return authorities;
    }
}