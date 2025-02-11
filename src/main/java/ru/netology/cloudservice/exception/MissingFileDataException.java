package ru.netology.cloudservice.exception;

public class MissingFileDataException extends RuntimeException {
    public MissingFileDataException(String message) {
        super(message);
    }
}