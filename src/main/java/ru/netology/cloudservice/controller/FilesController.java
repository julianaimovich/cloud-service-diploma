package ru.netology.cloudservice.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.exception.IllegalArgumentException;
import ru.netology.cloudservice.exception.MissingFileDataException;
import ru.netology.cloudservice.model.FilesEntity;
import ru.netology.cloudservice.service.FilesService;
import ru.netology.cloudservice.utils.Constants.Endpoints;

import java.util.List;

@RestController
public class FilesController {

    private final FilesService filesService;

    private static final Logger logger = LogManager.getLogger(FilesController.class);

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostMapping(path = Endpoints.FILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam(required = false) String filename,
                                             @RequestParam(required = false) MultipartFile file) {
        if (StringUtils.isBlank(filename)) {
            throw new MissingFileDataException("File name is missing");
        } else if (file.isEmpty()) {
            throw new MissingFileDataException("File content is missing");
        }
        logger.info("Uploading file '{}'", filename);
        filesService.saveFile(filename, file);
        return ResponseEntity.ok("File was uploaded successfully");
    }

    @GetMapping(Endpoints.FILE)
    public ResponseEntity<Resource> downloadFile(@RequestParam(required = false) String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new MissingFileDataException("File name is missing");
        }
        byte[] fileContent = filesService.getFile(filename);
        String contentType = filesService.getFileContentType(filename);
        logger.info("File '{}' was successfully downloaded", filename);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(new ByteArrayResource(fileContent));
    }

    @PutMapping(Endpoints.FILE)
    public ResponseEntity<String> editFile(@RequestParam(required = false) String filename,
                                           @RequestBody FileDto editFile) {
        if (StringUtils.isBlank(filename)) {
            throw new MissingFileDataException("File name is missing");
        }
        logger.info("Editing file '{}'", filename);
        filesService.editFile(filename, editFile.getFilename());
        return ResponseEntity.ok("File was successfully edited");
    }

    @DeleteMapping(Endpoints.FILE)
    public ResponseEntity<String> deleteFile(@RequestParam String filename) {
        logger.info("Deleting file '{}'", filename);
        filesService.deleteFile(filename);
        return ResponseEntity.ok("File was successfully deleted");
    }

    @GetMapping(Endpoints.GET_ALL_FILES)
    public List<FileDto> getAllFilesByLimit(@RequestParam(required = false) Integer limit) {
        if (limit == null) {
            return listOfFileEntitiesToDto(filesService.getAllFiles());
        } else if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }
        return listOfFileEntitiesToDto(filesService.getAllFilesByLimit(limit));
    }

    private List<FileDto> listOfFileEntitiesToDto(List<FilesEntity> fileEntitiesList) {
        return fileEntitiesList.stream().map(filesEntity ->
                new FileDto(filesEntity.getFilename(), filesEntity.getSize())).toList();
    }
}