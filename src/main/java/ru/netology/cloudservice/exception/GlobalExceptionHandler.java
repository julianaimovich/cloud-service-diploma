package ru.netology.cloudservice.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.netology.cloudservice.dto.ErrorDto;
import ru.netology.cloudservice.utils.Constants.ErrorMessages;

import java.io.FileNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorDto> handleFileUploadException(FileUploadException e) {
        logger.error("File upload error", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ErrorMessages.ERROR_INPUT_DATA));
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFound(FileNotFoundException e) {
        logger.error("File handle error", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.ERROR_INPUT_DATA);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGlobalException(Exception e) {
        logger.error("handle error", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}