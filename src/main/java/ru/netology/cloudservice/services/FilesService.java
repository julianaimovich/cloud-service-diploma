package ru.netology.cloudservice.services;

import org.springframework.stereotype.Service;
import ru.netology.cloudservice.entities.Files;
import ru.netology.cloudservice.repositories.FilesRepository;

@Service
public class FilesService {
    private final FilesRepository filesRepository;

    public FilesService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public void save(Files file) {
        filesRepository.save(file);
    }
}