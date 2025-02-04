package ru.netology.cloudservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.api.schemas.FileSchema;
import ru.netology.cloudservice.constants.ErrorMessages;
import ru.netology.cloudservice.db.entities.FilesEntity;
import ru.netology.cloudservice.db.repositories.FilesRepository;

import java.io.IOException;
import java.util.*;

@Service
public class FilesService {

    private final FilesRepository filesRepository;

    public FilesService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public void saveFile(String filename, MultipartFile file) throws IOException {
        FilesEntity fileEntity = FilesEntity.builder()
                .filename(filename)
                .contentType(file.getContentType())
                .size((int) file.getSize())
                .data(file.getBytes())
                .build();
        filesRepository.save(fileEntity);
    }

    public void editFile(String filename, String editFilename) throws IOException {
        Optional<FilesEntity> fileEntity = filesRepository.findByFilename(filename);
        if (fileEntity.isEmpty()) {
            throw new IOException(ErrorMessages.ERROR_UPLOAD_FILE);
        }
        FilesEntity fileForEdit = fileEntity.get();
        fileForEdit.setFilename(editFilename);
        filesRepository.save(fileForEdit);
    }

    public void deleteFile(String filename) throws IOException {
        Optional<FilesEntity> entity = filesRepository.findByFilename(filename);
        if (entity.isEmpty()) {
            throw new IOException(ErrorMessages.ERROR_DELETE_FILE);
        }
        FilesEntity fileForDelete = entity.get();
        filesRepository.deleteById(fileForDelete.getId());
    }

    public byte[] getFile(String filename) throws IOException {
        Optional<FilesEntity> entity = filesRepository.findByFilename(filename);
        if (entity.isEmpty()) {
            throw new IOException(ErrorMessages.ERROR_DOWNLOAD_FILE);
        }
        return entity.get().getData();
    }

    public String getFileContentType(String filename) throws IOException {
        Optional<FilesEntity> entity = filesRepository.findByFilename(filename);
        if (entity.isEmpty()) {
            throw new IOException(ErrorMessages.ERROR_DOWNLOAD_FILE);
        }
        return entity.get().getContentType();
    }

    public List<FileSchema> getAllFilesByLimit(Integer limit) {
        List<FilesEntity> fileEntitiesList = filesRepository.findAllByLimit(limit);
        if (!fileEntitiesList.isEmpty()) {
            return fileEntitiesList.stream().map(filesEntity ->
                    new FileSchema(filesEntity.getFilename(), filesEntity.getSize())).toList();
        }
        return Collections.emptyList();
    }
}