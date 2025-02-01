package ru.netology.cloudservice.api.controllers;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.api.schemas.BaseSchema;
import ru.netology.cloudservice.api.schemas.FileSchema;
import ru.netology.cloudservice.constants.Endpoints;
import ru.netology.cloudservice.constants.ErrorMessages;
import ru.netology.cloudservice.services.FilesService;

import java.io.IOException;
import java.util.List;

import static ru.netology.cloudservice.constants.RequestParamValues.FILE_CONTENT_TYPE;

@RestController
public class FilesController {

    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping(Endpoints.GET_ALL_FILES)
    public List<FileSchema> getAllFilesByLimit(@RequestParam Integer limit) throws BadRequestException {
        if (limit == null || limit <= 0) {
            throw new BadRequestException(ErrorMessages.ERROR_GETTING_FILE_LIST);
        }
        return filesService.getAllFilesByLimit(limit);
    }

    @PostMapping(path = Endpoints.FILE, consumes = FILE_CONTENT_TYPE)
    public ResponseEntity<BaseSchema> createFile(@RequestParam String filename,
                                                 @RequestBody MultipartFile file) throws IOException {
        if (filename == null || filename.isEmpty() || file == null) {
            throw new BadRequestException(ErrorMessages.ERROR_INPUT_DATA);
        }
        filesService.saveFile(filename, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(Endpoints.FILE)
    public ResponseEntity<BaseSchema> deleteFile(@RequestParam String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            throw new BadRequestException(ErrorMessages.ERROR_INPUT_DATA);
        }
        filesService.deleteFile(filename);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}