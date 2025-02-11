package ru.netology.cloudservice.exception;

public class FileProcessingException extends Exception {
    public FileProcessingException(String message, Throwable cause) {
        super("Failed to read bytes from file: " + message, cause);
    }
}