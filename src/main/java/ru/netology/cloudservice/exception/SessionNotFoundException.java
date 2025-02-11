package ru.netology.cloudservice.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException() {
        super("No active session found");
    }
}