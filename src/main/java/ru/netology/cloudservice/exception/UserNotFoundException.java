package ru.netology.cloudservice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super("User not found with login: " + login);
    }
}