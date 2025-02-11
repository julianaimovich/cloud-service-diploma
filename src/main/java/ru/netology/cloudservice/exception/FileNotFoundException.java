package ru.netology.cloudservice.exception;

public class FileNotFoundException extends Exception {
    public FileNotFoundException(String message) {
        super(String.format("File with name %s not found", message));
    }
}