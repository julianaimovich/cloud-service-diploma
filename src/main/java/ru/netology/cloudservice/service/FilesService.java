package ru.netology.cloudservice.service;

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

    public FilesService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public FilesEntity saveFile(String filename, MultipartFile file) {
        try {
            FilesEntity fileEntity = FilesEntity.builder()
                    .filename(filename)
                    .contentType(file.getContentType())
                    .size((int) file.getSize())
                    .data(file.getBytes())
                    .build();
            return filesRepository.save(fileEntity);
        } catch (FileProcessingException | IOException ex) {
            throw new FileProcessingException(filename, ex);
        }
    }

    public FilesEntity editFile(String filename, String editFilename) {
        FilesEntity fileEntity = filesRepository.findByFilename(filename)
                .orElseThrow(() ->
                        new FileNotFoundException(filename));
        fileEntity.setFilename(editFilename);
        return filesRepository.save(fileEntity);
    }

    public void deleteFile(String filename) {
        FilesEntity fileEntity = filesRepository.findByFilename(filename)
                .orElseThrow(() ->
                        new FileNotFoundException(filename));
        filesRepository.deleteById(fileEntity.getId());
    }

    public byte[] getFile(String filename) {
        FilesEntity fileEntity = filesRepository.findByFilename(filename)
                .orElseThrow(() ->
                        new FileNotFoundException(filename));
        return fileEntity.getData();
    }

    public String getFileContentType(String filename) {
        FilesEntity fileEntity = filesRepository.findByFilename(filename)
                .orElseThrow(() ->
                        new FileNotFoundException(filename));
        return fileEntity.getContentType();
    }

    public List<FilesEntity> getAllFilesByLimit(Integer limit) {
        List<FilesEntity> fileEntitiesList = filesRepository.findAllByLimit(limit);
        if (!fileEntitiesList.isEmpty()) {
            return fileEntitiesList;
        }
        return Collections.emptyList();
    }

    public List<FilesEntity> getAllFiles() {
        List<FilesEntity> fileEntitiesList = filesRepository.findAll();
        if (!fileEntitiesList.isEmpty()) {
            return fileEntitiesList;
        }
        return Collections.emptyList();
    }
}