package ru.netology.cloudservice.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.cloudservice.entities.Users;
import ru.netology.cloudservice.repositories.UsersRepository;
import ru.netology.cloudservice.schemas.UserPrincipalSchema;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UsersRepository usersRepository;

    @Override
    public UserPrincipalSchema loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipalSchema(user);
    }
}