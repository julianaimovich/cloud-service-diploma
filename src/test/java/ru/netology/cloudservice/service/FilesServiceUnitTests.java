package ru.netology.cloudservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.model.FilesEntity;
import ru.netology.cloudservice.repository.FilesRepository;
import ru.netology.cloudservice.util.MockUtil;
import ru.netology.cloudservice.util.TestConstants.FilesParamValues;
import ru.netology.cloudservice.util.builder.FileBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FilesServiceUnitTests {

    @Mock
    private FilesRepository filesRepository;
    @InjectMocks
    private FilesService filesService;

    @Test
    @DisplayName("Save file with \".jpg\" extension")
    public void saveFileWithJpgExtensionTest() throws IOException, URISyntaxException {
        // Given
        FilesEntity expectedFile = FileBuilder.getJpgFileEntity();
        expectedFile.setId(FilesParamValues.FILE_DEFAULT_ID);
        MockMultipartFile fileContent = new MockMultipartFile("parrot",
                expectedFile.getFilename(), MediaType.IMAGE_JPEG_VALUE, expectedFile.getData());
        when(filesRepository.save(any(FilesEntity.class))).thenAnswer
                (MockUtil.saveWithId(expectedFile.getId()));
        // When
        FilesEntity result = filesService.saveFile(expectedFile.getFilename(), fileContent);
        // Then
        assertNotNull(result);
        assertEquals(expectedFile.getFilename(), result.getFilename());
        verify(filesRepository, Mockito.times(1)).save(any(FilesEntity.class));
    }

    @Test
    @DisplayName("Save file with \".txt\" extension")
    public void saveFileWithTxtExtensionTest() throws IOException, URISyntaxException {
        // Given
        FilesEntity expectedFile = FileBuilder.getTxtFileEntity();
        expectedFile.setId(FilesParamValues.FILE_DEFAULT_ID);
        MockMultipartFile fileContent = new MockMultipartFile("story",
                expectedFile.getFilename(), MediaType.TEXT_PLAIN_VALUE, expectedFile.getData());
        when(filesRepository.save(any(FilesEntity.class))).thenAnswer
                (MockUtil.saveWithId(expectedFile.getId()));
        // When
        FilesEntity result = filesService.saveFile(expectedFile.getFilename(), fileContent);
        // Then
        assertNotNull(result);
        assertEquals(expectedFile.getFilename(), result.getFilename());
        verify(filesRepository, Mockito.times(1)).save(any(FilesEntity.class));
    }

    @Test
    @DisplayName("Get file content")
    public void getFileContentTest() throws IOException, URISyntaxException {
        // Given
        FilesEntity file = FileBuilder.getTxtFileEntity();
        file.setId(FilesParamValues.FILE_DEFAULT_ID);
        when(filesRepository.findByFilename(file.getFilename())).thenReturn(Optional.of(file));
        // When
        byte[] result = filesService.getFile(file.getFilename());
        // Then
        assertNotNull(result);
        assertEquals(file.getData(), result);
        verify(filesRepository, Mockito.times(1))
                .findByFilename(file.getFilename());
    }

    @Test
    @DisplayName("Get file content type")
    public void getFileContentTypeTest() throws IOException, URISyntaxException {
        // Given
        FilesEntity file = FileBuilder.getTxtFileEntity();
        file.setId(FilesParamValues.FILE_DEFAULT_ID);
        when(filesRepository.findByFilename(file.getFilename())).thenReturn(Optional.of(file));
        // When
        String result = filesService.getFileContentType(file.getFilename());
        // Then
        assertNotNull(result);
        assertEquals(file.getContentType(), result);
        verify(filesRepository, Mockito.times(1))
                .findByFilename(file.getFilename());
    }

    @Test
    @DisplayName("Get all files by limit")
    public void getAllFilesByLimitTest() throws IOException, URISyntaxException {
        // Given
        List<FilesEntity> filesList = FileBuilder.getFilesEntityList();
        IntStream.range(0, filesList.size()).forEach
                (i -> filesList.get(i).setId((long) i));
        Integer limit = FileBuilder.faker.number().numberBetween(1, filesList.size() - 1);
        List<FilesEntity> expectedFilesList = filesList.subList(0, limit);
        when(filesRepository.findAllByLimit(limit)).thenReturn(expectedFilesList);
        // When
        List<FileDto> result = filesService.getAllFilesByLimit(limit);
        // Then
        assertNotNull(result);
        assertEquals(expectedFilesList.size(), result.size());
        verify(filesRepository, Mockito.times(1))
                .findAllByLimit(limit);
    }

    @Test
    @DisplayName("Edit file name")
    public void editFileNameTest() throws IOException, URISyntaxException {
        // Given
        FilesEntity file = FileBuilder.getTxtFileEntity();
        String previousName = file.getFilename();
        String newName = "test.txt";
        file.setId(FilesParamValues.FILE_DEFAULT_ID);
        when(filesRepository.findByFilename(previousName)).thenReturn(Optional.of(file));
        file.setFilename(newName);
        when(filesRepository.save(file)).thenReturn(file);
        // When
        FilesEntity result = filesService.editFile(previousName, newName);
        // Then
        assertNotNull(result);
        assertEquals(file.getFilename(), result.getFilename());
        verify(filesRepository, Mockito.times(1))
                .findByFilename(previousName);
        verify(filesRepository, Mockito.times(1)).save(file);
    }

    @Test
    @DisplayName("Delete file")
    public void deleteFileTest() throws IOException, URISyntaxException {
        // Given
        FilesEntity file = FileBuilder.getTxtFileEntity();
        file.setId(FilesParamValues.FILE_DEFAULT_ID);
        when(filesRepository.findByFilename(file.getFilename())).thenReturn(Optional.of(file));
        willDoNothing().given(filesRepository).deleteById(file.getId());
        // When
        filesService.deleteFile(file.getFilename());
        // Then
        verify(filesRepository, Mockito.times(1))
                .findByFilename(file.getFilename());
        verify(filesRepository, Mockito.times(1))
                .deleteById(file.getId());
    }
}