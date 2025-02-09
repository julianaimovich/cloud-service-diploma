package ru.netology.cloudservice.config;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.netology.cloudservice.config.Constants.ErrorMessages;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<String> handleFileUploadException(FileUploadException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.ERROR_INPUT_DATA);
    }
}