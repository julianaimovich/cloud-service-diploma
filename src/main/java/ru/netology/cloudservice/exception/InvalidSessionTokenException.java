package ru.netology.cloudservice.exception;

public class InvalidSessionTokenException extends RuntimeException {
    public InvalidSessionTokenException() {
        super("Invalid session token");
    }
}