package ru.netology.cloudservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.cloudservice.entities.Users;
import ru.netology.cloudservice.repositories.UsersRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username));
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new Users(user.getUsername(), user.getPassword());
    }
}