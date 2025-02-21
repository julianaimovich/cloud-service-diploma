package ru.netology.cloudservice.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.exception.FileNotFoundException;
import ru.netology.cloudservice.exception.FileProcessingException;
import ru.netology.cloudservice.model.FilesEntity;
import ru.netology.cloudservice.repository.FilesRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class FilesService {

    private final FilesRepository filesRepository;

    private static final Logger logger = LogManager.getLogger(FilesService.class);

    public FilesService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public FilesEntity saveFile(String filename, MultipartFile file) {
        try {
            logger.info("Trying to save file: {}", filename);
            FilesEntity fileEntity = FilesEntity.builder()
                    .filename(filename)
                    .contentType(file.getContentType())
                    .size((int) file.getSize())
                    .data(file.getBytes())
                    .build();
            logger.info("File with name '{}' has been saved", filename);
            return filesRepository.save(fileEntity);
        } catch (FileProcessingException | IOException ex) {
            throw new FileProcessingException(filename, ex);
        }
    }

    public FilesEntity editFile(String filename, String editFilename) {
        logger.info("Fetching file from DB with name: {}", filename);
        FilesEntity fileEntity = filesRepository.findByFilename(filename)
                .orElseThrow(() ->
                        new FileNotFoundException(filename));
        fileEntity.setFilename(editFilename);
        logger.info("File name has been edited from {} to {}", filename, editFilename);
        return filesRepository.save(fileEntity);
    }

    public void deleteFile(String filename) {
        logger.info("Fetching file from DB with name: {}", filename);
        FilesEntity fileEntity = filesRepository.findByFilename(filename)
                .orElseThrow(() ->
                        new FileNotFoundException(filename));
        logger.info("File with name '{}' was successfully deleted", filename);
        filesRepository.deleteById(fileEntity.getId());
    }

    public byte[] getFile(String filename) {
        logger.info("Fetching file from DB with name: {}", filename);
        FilesEntity fileEntity = filesRepository.findByFilename(filename)
                .orElseThrow(() ->
                        new FileNotFoundException(filename));
        logger.info("Content from file with name '{}' was successfully fetched", filename);
        return fileEntity.getData();
    }

    public String getFileContentType(String filename) {
        logger.info("Fetching file from DB with name: {}", filename);
        FilesEntity fileEntity = filesRepository.findByFilename(filename)
                .orElseThrow(() ->
                        new FileNotFoundException(filename));
        String contentType = fileEntity.getContentType();
        logger.info("Content type of the fetched file is {}", contentType);
        return contentType;
    }

    public List<FilesEntity> getAllFilesByLimit(Integer limit) {
        logger.info("Fetching files from DB with limit: {}", limit);
        List<FilesEntity> fileEntitiesList = filesRepository.findAllByLimit(limit);
        if (!fileEntitiesList.isEmpty()) {
            logger.info("Found {} files", fileEntitiesList.size());
            return fileEntitiesList;
        }
        logger.info("No files found");
        return Collections.emptyList();
    }

    public List<FilesEntity> getAllFiles() {
        logger.info("Fetching all files from DB");
        List<FilesEntity> fileEntitiesList = filesRepository.findAll();
        if (!fileEntitiesList.isEmpty()) {
            logger.info("Found {} files", fileEntitiesList.size());
            return fileEntitiesList;
        }
        logger.info("No files found");
        return Collections.emptyList();
    }
}