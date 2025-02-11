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
import java.util.Optional;

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
        } catch (IOException ex) {
            throw new FileProcessingException("Failed to read bytes from file: " + filename, ex);
        }
    }

    public FilesEntity editFile(String filename, String editFilename) {
        Optional<FilesEntity> fileEntity = filesRepository.findByFilename(filename);
        if (fileEntity.isEmpty()) {
            throw new FileNotFoundException("File with name %s not found", filename);
        }
        FilesEntity fileForEdit = fileEntity.get();
        fileForEdit.setFilename(editFilename);
        return filesRepository.save(fileForEdit);
    }

    public void deleteFile(String filename) {
        Optional<FilesEntity> fileEntity = filesRepository.findByFilename(filename);
        if (fileEntity.isEmpty()) {
            throw new FileNotFoundException("File with name %s not found", filename);
        }
        FilesEntity fileForDelete = fileEntity.get();
        filesRepository.deleteById(fileForDelete.getId());
    }

    public byte[] getFile(String filename) {
        Optional<FilesEntity> fileEntity = filesRepository.findByFilename(filename);
        if (fileEntity.isEmpty()) {
            throw new FileNotFoundException("File with name %s not found", filename);
        }
        return fileEntity.get().getData();
    }

    public String getFileContentType(String filename) {
        Optional<FilesEntity> fileEntity = filesRepository.findByFilename(filename);
        if (fileEntity.isEmpty()) {
            throw new FileNotFoundException("File with name %s not found", filename);
        }
        return fileEntity.get().getContentType();
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