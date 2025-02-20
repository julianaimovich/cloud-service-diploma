package ru.netology.cloudservice.exception;

public class InvalidSessionTokenException extends Exception {
    public InvalidSessionTokenException() {
        super("Invalid session token");
    }
}