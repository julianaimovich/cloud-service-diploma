package ru.netology.cloudservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.netology.cloudservice.dto.ErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ErrorDto> handleSessionNotFoundException(SessionNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(InvalidSessionTokenException.class)
    public ResponseEntity<ErrorDto> handleInvalidSessionTokenException(InvalidSessionTokenException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorDto> handleFileNotFoundException(FileNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ErrorDto> handleFileProcessingException(FileProcessingException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MissingFileDataException.class)
    public ResponseEntity<ErrorDto> handleMissingFileDateException(MissingFileDataException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}