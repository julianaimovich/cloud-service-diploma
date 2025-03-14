package ru.netology.cloudservice.util.builder;

import net.datafaker.Faker;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.mock.web.MockMultipartFile;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.model.FilesEntity;
import ru.netology.cloudservice.util.ResourceLoader;
import ru.netology.cloudservice.util.TestConstants.FilesParamValues;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class FileBuilder {

    public static final Faker faker = new Faker();

    public static FilesEntity getJpgFileEntity() throws IOException, URISyntaxException {
        File file = ResourceLoader.getFileFromResources(FilesParamValues.FILE_JPG_PATH);
        return FilesEntity.builder()
                .filename(file.getName())
                .contentType(MediaType.IMAGE_JPEG_VALUE)
                .size((int) file.length())
                .data(FileUtils.readFileToByteArray(file))
                .build();
    }

    public static FilesEntity getTxtFileEntity() throws IOException, URISyntaxException {
        File file = ResourceLoader.getFileFromResources(FilesParamValues.FILE_TXT_PATH);
        return FilesEntity.builder()
                .filename(file.getName())
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .size((int) file.length())
                .data(FileUtils.readFileToByteArray(file))
                .build();
    }

    public static List<FilesEntity> getFilesEntityList() throws IOException, URISyntaxException {
        List<FilesEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(getJpgFileEntity());
            list.add(getTxtFileEntity());
        }
        return list;
    }

    public static FileDto getJpgFileForRequest() throws URISyntaxException {
        File file = ResourceLoader.getFileFromResources(FilesParamValues.FILE_JPG_PATH);
        return FileDto.builder()
                .filename(file.getName())
                .file(file)
                .contentType(MediaType.IMAGE_JPEG_VALUE)
                .build();
    }

    public static FileDto getTxtFileForRequest() throws URISyntaxException {
        File file = ResourceLoader.getFileFromResources(FilesParamValues.FILE_TXT_PATH);
        return FileDto.builder()
                .filename(file.getName())
                .file(file)
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .build();
    }

    public static MockMultipartFile fileToMultipartFile(File file, String contentType) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return new MockMultipartFile(
                    FilesParamValues.FILE_PARAM,
                    file.getName(),
                    contentType,
                    fileInputStream);
        }
    }

    public static FilesEntity fileDtoToEntity(FileDto fileDto) throws IOException {
        File fileContent = fileDto.getFile();
        MediaType contentType = MediaTypeFactory.getMediaType(fileContent.getName()).orElseThrow();
        return FilesEntity.builder()
                .id(FilesParamValues.FILE_DEFAULT_ID)
                .filename(fileDto.getFilename())
                .contentType(contentType.getType())
                .size(fileDto.getSize())
                .data(FileUtils.readFileToByteArray(fileContent))
                .build();
    }
}