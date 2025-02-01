package ru.netology.cloudservice.services;

import org.springframework.stereotype.Service;
import ru.netology.cloudservice.api.schemas.FileSchema;
import ru.netology.cloudservice.db.entities.FilesEntity;
import ru.netology.cloudservice.db.repositories.FilesRepository;

import java.util.Collections;
import java.util.List;

@Service
public class FileService {

    private final FilesRepository filesRepository;

    public FileService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public List<FileSchema> getAllFilesByLimit(Integer limit) {
        List<FilesEntity> fileEntitiesList = filesRepository.findAllByLimit(limit);
        if (!fileEntitiesList.isEmpty()) {
            return fileEntitiesList.stream().map(filesEntity ->
                    new FileSchema(filesEntity.getFilename(), filesEntity.getSize())).toList();
        } else {
            return Collections.emptyList();
        }
    }
}