package ru.netology.cloudservice.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String login) {
        super(String.format("User with login %s not found", login));
    }
}