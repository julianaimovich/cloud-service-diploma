package ru.netology.cloudservice.services;

import org.springframework.stereotype.Service;
import ru.netology.cloudservice.api.schemas.FileSchema;
import ru.netology.cloudservice.db.entities.FileEntity;
import ru.netology.cloudservice.db.repositories.FileRepository;

import java.util.Collections;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<FileSchema> getAllFilesByLimit(Integer limit) {
        List<FileEntity> fileEntitiesList = fileRepository.findAllByLimit(limit);
        if (!fileEntitiesList.isEmpty()) {
            return fileEntitiesList.stream().map(fileEntity ->
                    new FileSchema(fileEntity.getFilename(), fileEntity.getSize())).toList();
        } else {
            return Collections.emptyList();
        }
    }
}