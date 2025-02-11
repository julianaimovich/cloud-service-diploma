package ru.netology.cloudservice.exception;

public class SessionNotFoundException extends Exception {
    public SessionNotFoundException() {
        super("No active session found");
    }
}