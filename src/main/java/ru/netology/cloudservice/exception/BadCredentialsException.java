package ru.netology.cloudservice.exception;

public class BadCredentialsException extends Exception {
    public BadCredentialsException() {
        super("User with the specified credentials does not exist");
    }
}