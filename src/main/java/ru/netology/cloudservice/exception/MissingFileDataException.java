package ru.netology.cloudservice.exception;

public class MissingFileDataException extends Exception {
    public MissingFileDataException(String message) {
        super(message);
    }
}