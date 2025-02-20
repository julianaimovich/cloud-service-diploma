package ru.netology.cloudservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.netology.cloudservice.dto.ErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
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
    public ResponseEntity<ErrorDto> handleMissingFileDataException(MissingFileDataException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorDto> handleMultipartException(MultipartException ex) {
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGlobalException(Exception ex) {
        logger.error("Unhandled exception: ", ex);
        ErrorDto errorDto = new ErrorDto("Internal Server Error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}