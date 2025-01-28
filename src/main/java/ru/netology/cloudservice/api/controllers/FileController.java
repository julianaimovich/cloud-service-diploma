package ru.netology.cloudservice.api.controllers;

import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudservice.services.FileService;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /*@GetMapping("/list")
    public ResponseEntity<List<Files>> list() {}*/
}