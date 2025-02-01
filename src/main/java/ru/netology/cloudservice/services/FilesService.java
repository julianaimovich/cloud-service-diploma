package ru.netology.cloudservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.api.schemas.FileSchema;
import ru.netology.cloudservice.constants.ErrorMessages;
import ru.netology.cloudservice.db.entities.FilesEntity;
import ru.netology.cloudservice.db.repositories.FilesRepository;

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

    public List<FileSchema> getAllFilesByLimit(Integer limit) {
        List<FilesEntity> fileEntitiesList = filesRepository.findAllByLimit(limit);
        if (!fileEntitiesList.isEmpty()) {
            return fileEntitiesList.stream().map(filesEntity ->
                    new FileSchema(filesEntity.getFilename(), filesEntity.getSize())).toList();
        }
        return Collections.emptyList();
    }

    public void saveFile(String filename, MultipartFile file) throws IOException {
        FilesEntity fileEntity = FilesEntity.builder()
                .filename(filename)
                .fileContent(file.getBytes())
                .size((int) file.getSize())
                .build();
        filesRepository.save(fileEntity);
    }

    public void deleteFile(String filename) throws IOException {
        Optional<FilesEntity> file = filesRepository.findByFilename(filename);
        if (!file.isPresent()) {
            throw new IOException(ErrorMessages.ERROR_DELETE_FILE);
        }
        filesRepository.deleteById(file.orElseThrow().getId());
    }
}