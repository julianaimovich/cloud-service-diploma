package ru.netology.cloudservice.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message, String filename) {
        super(String.format(message, filename));
    }
}