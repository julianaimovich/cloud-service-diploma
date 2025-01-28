package ru.netology.cloudservice.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.cloudservice.api.schemas.FileSchema;
import ru.netology.cloudservice.services.FileService;

import java.util.List;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /*@GetMapping("/list")
    public ResponseEntity<List<Files>> list() {}*/

    @GetMapping("/list")
    public List<FileSchema> getAllFilesByLimit(@RequestHeader("auth-token") String authToken, @RequestParam Integer limit) {
        if (authToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized error");
        } else if (limit <= 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
        return fileService.getAllFilesByLimit(limit);
    }
}