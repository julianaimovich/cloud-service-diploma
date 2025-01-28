package ru.netology.cloudservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudservice.entities.Files;
import ru.netology.cloudservice.schemaBuilders.ErrorSchemaBuilder;
import ru.netology.cloudservice.schemas.ResponseSchema;
import ru.netology.cloudservice.services.FilesService;

@RestController
public class FilesController {
    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostMapping("/file")
    public ResponseEntity<ResponseSchema> uploadFile(@RequestHeader String authToken, @RequestHeader String fileName) {
        if (fileName.isEmpty()) {
            return new ResponseEntity<>(ErrorSchemaBuilder.inputDataError(), HttpStatus.BAD_REQUEST);
        } else if (authToken.isEmpty()) {
            return new ResponseEntity<>(ErrorSchemaBuilder.unauthorizedError(), HttpStatus.UNAUTHORIZED);
        }
        Files file = Files.builder().fileName(fileName).build();
        filesService.save(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}