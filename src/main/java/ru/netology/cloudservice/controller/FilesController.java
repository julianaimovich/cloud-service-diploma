package ru.netology.cloudservice.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.config.Constants.Endpoints;
import ru.netology.cloudservice.config.Constants.ErrorMessages;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.service.FilesService;

import java.io.IOException;
import java.util.List;

@RestController
public class FilesController {

    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostMapping(path = Endpoints.FILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> uploadFile(@RequestParam String filename,
                                                 @RequestParam MultipartFile file) throws IOException {
        if (StringUtils.isBlank(filename) || file.isEmpty()) {
            throw new BadRequestException(ErrorMessages.ERROR_INPUT_DATA);
        }
        filesService.saveFile(filename, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(Endpoints.FILE)
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            throw new BadRequestException(ErrorMessages.ERROR_INPUT_DATA);
        }
        byte[] fileContent = filesService.getFile(filename);
        String contentType = filesService.getFileContentType(filename);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(new ByteArrayResource(fileContent));
    }

    @PutMapping(Endpoints.FILE)
    public ResponseEntity<HttpStatus> editFile(@RequestParam String filename,
                                               @RequestBody FileDto editFile) throws IOException {
        if (StringUtils.isBlank(filename) || editFile == null) {
            throw new BadRequestException(ErrorMessages.ERROR_INPUT_DATA);
        }
        filesService.editFile(filename, editFile.getFilename());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(Endpoints.FILE)
    public ResponseEntity<HttpStatus> deleteFile(@RequestParam String filename) throws IOException {
        if (StringUtils.isBlank(filename)) {
            throw new BadRequestException(ErrorMessages.ERROR_INPUT_DATA);
        }
        filesService.deleteFile(filename);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(Endpoints.GET_ALL_FILES)
    public List<FileDto> getAllFilesByLimit(@RequestParam(required = false) Integer limit) throws BadRequestException {
        if (limit == null) {
            return filesService.getAllFiles();
        } else if (limit <= 0) {
            throw new BadRequestException(ErrorMessages.ERROR_GETTING_FILE_LIST);
        }
        return filesService.getAllFilesByLimit(limit);
    }
}