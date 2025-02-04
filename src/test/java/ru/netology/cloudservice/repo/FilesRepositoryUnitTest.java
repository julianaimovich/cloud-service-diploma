package ru.netology.cloudservice.repo;

import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import ru.netology.cloudservice.model.FilesEntity;
import ru.netology.cloudservice.repository.FilesRepository;
import ru.netology.cloudservice.util.TestConstants;
import ru.netology.cloudservice.util.builder.FilesEntityBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles({"test"})
public class FilesRepositoryUnitTest {

    @Autowired
    private FilesRepository filesRepository;
    private final Faker faker = new Faker();

    @Test
    @Rollback
    @DisplayName("Save file with \".jpg\" extension")
    public void saveFileWithJpgExtensionTest() throws IOException, URISyntaxException {
        FilesEntity file = filesRepository.save(FilesEntityBuilder.getJpgFileEntity());
        List<FilesEntity> allFilesInSystem = filesRepository.findAll();
        assertTrue(allFilesInSystem.contains(file));
    }

    @Test
    @Rollback
    @DisplayName("Save file with \".txt\" extension")
    public void saveFileWithTxtExtensionTest() throws IOException, URISyntaxException {
        FilesEntity file = filesRepository.save(FilesEntityBuilder.getTxtFileEntity());
        List<FilesEntity> allFilesInSystem = filesRepository.findAll();
        assertTrue(allFilesInSystem.contains(file));
    }

    @Test
    @DisplayName("Unable to save file without file name")
    public void unableToSaveFileWithoutFileNameTest() throws IOException, URISyntaxException {
        FilesEntity file = FilesEntityBuilder.getTxtFileEntity();
        file.setFilename(null);
        DataIntegrityViolationException exception = assertThrows
                (DataIntegrityViolationException.class, () -> filesRepository.save(file));
        assertTrue(exception.getMessage().contains(TestConstants.ExceptionMessages.NOT_NULL_PROPERTY_NULL_REFERENCE));
    }

    @Test
    @DisplayName("Unable to save file without file content")
    public void unableToSaveFileWithoutFileContentTest() throws IOException, URISyntaxException {
        FilesEntity file = FilesEntityBuilder.getTxtFileEntity();
        file.setData(null);
        DataIntegrityViolationException exception = assertThrows
                (DataIntegrityViolationException.class, () -> filesRepository.save(file));
        assertTrue(exception.getMessage().contains(TestConstants.ExceptionMessages.NOT_NULL_PROPERTY_NULL_REFERENCE));
    }

    @Test
    @Rollback
    @DisplayName("Get file by id")
    public void getFileByIdTest() throws IOException, URISyntaxException {
        FilesEntity file = filesRepository.save(FilesEntityBuilder.getTxtFileEntity());
        FilesEntity fileFromDb = filesRepository.findById(file.getId()).orElseThrow();
        assertEquals(file, fileFromDb);
    }

    @Test
    @Rollback
    @DisplayName("Get file by file name")
    public void getFileByFileNameTest() throws IOException, URISyntaxException {
        FilesEntity file = filesRepository.save(FilesEntityBuilder.getJpgFileEntity());
        FilesEntity fileFromDb = filesRepository.findByFilename(file.getFilename()).orElseThrow();
        assertEquals(file, fileFromDb);
    }

    @Test
    @Rollback
    @DisplayName("Get all existing files")
    public void getAllFilesTest() throws IOException, URISyntaxException {
        filesRepository.saveAll(FilesEntityBuilder.getFilesEntityList());
        List<FilesEntity> filesList = filesRepository.findAll();
        assertThat(filesList.size()).isGreaterThan(0);
    }

    @Test
    @Rollback
    @DisplayName("Get all files by limit")
    public void getAllFilesByLimitTest() throws IOException, URISyntaxException {
        List<FilesEntity> filesList = filesRepository.saveAll(FilesEntityBuilder.getFilesEntityList());
        Integer limit = faker.number().numberBetween(1, filesList.size() - 1);
        List<FilesEntity> filesListByLimit = filesRepository.findAllByLimit(limit);
        assertEquals(limit, filesListByLimit.size());
    }

    @Test
    @Rollback
    @DisplayName("Update file")
    public void updateFileTest() throws IOException, URISyntaxException {
        FilesEntity file = filesRepository.save(FilesEntityBuilder.getTxtFileEntity());
        String randomFileFullPath = faker.file().fileName();
        String fileName = Paths.get(randomFileFullPath).getFileName().toString();
        file.setFilename(fileName);
        FilesEntity fileUpdated =  filesRepository.save(file);
        assertEquals(file.getFilename(), fileUpdated.getFilename());
    }

    @Test
    @DisplayName("Delete file")
    public void deleteUserTest() throws IOException, URISyntaxException {
        FilesEntity file = filesRepository.save(FilesEntityBuilder.getJpgFileEntity());
        filesRepository.delete(file);
        Optional<FilesEntity> optional = filesRepository.findById(file.getId());
        assertTrue(optional.isEmpty());
    }
}