package ru.netology.cloudservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.netology.cloudservice.dto.ErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(BadCredentialsException ex) {
        logger.error("BadCredentialsException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ErrorDto> handleSessionNotFoundException(SessionNotFoundException ex) {
        logger.error("SessionNotFoundException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(InvalidSessionTokenException.class)
    public ResponseEntity<ErrorDto> handleInvalidSessionTokenException(InvalidSessionTokenException ex) {
        logger.error("InvalidSessionTokenException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("UserNotFoundException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorDto> handleFileNotFoundException(FileNotFoundException ex) {
        logger.error("FileNotFoundException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ErrorDto> handleFileProcessingException(FileProcessingException ex) {
        logger.error("FileProcessingException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MissingFileDataException.class)
    public ResponseEntity<ErrorDto> handleMissingFileDataException(MissingFileDataException ex) {
        logger.error("MissingFileDataException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("IllegalArgumentException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorDto> handleMultipartException(MultipartException ex) {
        logger.error("MultipartException: {}", ex.getMessage(), ex);
        String message;
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof org.apache.tomcat.util.http.fileupload.FileUploadException) {
            if (rootCause.getMessage().contains("no multipart boundary was found")) {
                message = "Error: multipart boundary is missing. Check the request format";
            } else {
                message = "File upload error: " + rootCause.getMessage();
            }
        } else {
            message = "Error processing the multipart request: " + ex.getMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(message));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        logger.error("HttpMessageNotReadableException: {}", ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto("Required request body is missing");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers,
                                                                          HttpStatusCode status, WebRequest request) {
        logger.error("MissingServletRequestParameterException: {}", ex.getMessage(), ex);
        String message = String.format("Required request parameter '%s' is missing", ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGlobalException(Exception ex) {
        logger.error("Unhandled exception: ", ex);
        ErrorDto errorDto = new ErrorDto("Internal Server Error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}