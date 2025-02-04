package ru.netology.cloudservice.util;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

public class ResourceLoader {

    public static File getFileFromResources(String filePath) throws URISyntaxException {
        return new File(Objects.requireNonNull(ResourceLoader.class
                .getClassLoader().getResource(filePath)).toURI());
    }
}