package ru.netology.cloudservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.model.FilesEntity;
import ru.netology.cloudservice.repository.FilesRepository;
import ru.netology.cloudservice.utils.Constants.ErrorMessages;

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

    public FilesEntity saveFile(String filename, MultipartFile file) throws IOException {
        FilesEntity fileEntity = FilesEntity.builder()
                .filename(filename)
                .contentType(file.getContentType())
                .size((int) file.getSize())
                .data(file.getBytes())
                .build();
        return filesRepository.save(fileEntity);
    }

    public FilesEntity editFile(String filename, String editFilename) throws IOException {
        Optional<FilesEntity> fileEntity = filesRepository.findByFilename(filename);
        if (fileEntity.isEmpty()) {
            throw new IOException(ErrorMessages.ERROR_UPLOAD_FILE);
        }
        FilesEntity fileForEdit = fileEntity.get();
        fileForEdit.setFilename(editFilename);
        return filesRepository.save(fileForEdit);
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

    public List<FileDto> getAllFilesByLimit(Integer limit) {
        List<FilesEntity> fileEntitiesList = filesRepository.findAllByLimit(limit);
        if (!fileEntitiesList.isEmpty()) {
            return listOfFileEntitiesToDto(fileEntitiesList);
        }
        return Collections.emptyList();
    }

    public List<FileDto> getAllFiles() {
        List<FilesEntity> fileEntitiesList = filesRepository.findAll();
        if (!fileEntitiesList.isEmpty()) {
            return listOfFileEntitiesToDto(fileEntitiesList);
        }
        return Collections.emptyList();
    }

    private List<FileDto> listOfFileEntitiesToDto(List<FilesEntity> fileEntitiesList) {
        return fileEntitiesList.stream().map(filesEntity ->
                new FileDto(filesEntity.getFilename(), filesEntity.getSize())).toList();
    }
}