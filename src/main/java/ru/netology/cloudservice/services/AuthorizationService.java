package ru.netology.cloudservice.services;

import org.springframework.stereotype.Service;
import ru.netology.cloudservice.entities.Users;
import ru.netology.cloudservice.repositories.UsersRepository;

import java.util.List;

@Service
public class AuthorizationService {
    private final UsersRepository usersRepository;

    public AuthorizationService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }
}