package ru.netology.cloudservice.services;

import org.springframework.stereotype.Service;
import ru.netology.cloudservice.entities.Users;
import ru.netology.cloudservice.repositories.UsersRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Optional<Users> findUserByLoginAndPassword(String login, String password) {
        return usersRepository.findByLoginAndPassword(login, password);
    }
}