package ru.netology.cloudservice.services;

import org.springframework.stereotype.Service;
import ru.netology.cloudservice.db.entities.FileEntity;
import ru.netology.cloudservice.db.repositories.FileRepository;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void save(FileEntity fileEntity) {
        fileRepository.save(fileEntity);
    }
}