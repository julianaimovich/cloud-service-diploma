package ru.netology.cloudservice.util.builder;

import net.datafaker.Faker;
import org.apache.commons.io.FileUtils;
import ru.netology.cloudservice.model.FilesEntity;
import ru.netology.cloudservice.util.ResourceLoader;
import ru.netology.cloudservice.util.TestConstants.FilesParamValues;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class FilesEntityBuilder {

    public static final Faker faker = new Faker();

    public static FilesEntity getJpgFileEntity() throws IOException, URISyntaxException {
        File file = ResourceLoader.getFileFromResources(FilesParamValues.FILE_JPG_PATH);
        return FilesEntity.builder()
                .filename(file.getName())
                .contentType(FilesParamValues.IMAGE_CONTENT_TYPE)
                .size((int) file.length())
                .data(FileUtils.readFileToByteArray(file))
                .build();
    }

    public static FilesEntity getTxtFileEntity() throws IOException, URISyntaxException {
        File file = ResourceLoader.getFileFromResources(FilesParamValues.FILE_TXT_PATH);
        return FilesEntity.builder()
                .filename(file.getName())
                .contentType(FilesParamValues.TEXT_CONTENT_TYPE)
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
}