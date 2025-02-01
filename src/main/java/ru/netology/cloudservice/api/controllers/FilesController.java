package ru.netology.cloudservice.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.cloudservice.api.schemas.BaseSchema;
import ru.netology.cloudservice.api.schemas.FileSchema;
import ru.netology.cloudservice.constants.Endpoints;
import ru.netology.cloudservice.constants.ErrorDescriptions;
import ru.netology.cloudservice.services.FilesService;

import java.io.IOException;
import java.util.List;

import static ru.netology.cloudservice.constants.RequestParamValues.AUTH_TOKEN_HEADER;
import static ru.netology.cloudservice.constants.RequestParamValues.FILE_CONTENT_TYPE;

@RestController
public class FilesController {

    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping(Endpoints.GET_ALL_FILES)
    public List<FileSchema> getAllFilesByLimit(@RequestHeader(AUTH_TOKEN_HEADER) String authToken,
                                               @RequestParam Integer limit) {
        if (authToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorDescriptions.UNAUTHORIZED);
        } else if (limit <= 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorDescriptions.INTERNAL_SERVER_ERROR);
        }
        return filesService.getAllFilesByLimit(limit);
    }

    @PostMapping(path = Endpoints.FILE, consumes = FILE_CONTENT_TYPE)
    public ResponseEntity<BaseSchema> createFile(@RequestHeader(AUTH_TOKEN_HEADER) String authToken,
                                                 @RequestParam String filename,
                                                 @RequestBody MultipartFile file) throws IOException {
        if (authToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorDescriptions.UNAUTHORIZED);
        } else if (filename.isEmpty() || file == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorDescriptions.ERROR_INPUT_DATA);
        }
        filesService.saveFile(filename, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}