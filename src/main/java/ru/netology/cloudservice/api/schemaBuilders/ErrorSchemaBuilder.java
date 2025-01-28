package ru.netology.cloudservice.api.schemaBuilders;

import ru.netology.cloudservice.api.schemas.ErrorSchema;

public class ErrorSchemaBuilder {

    private static int LAST_ERROR_ID = 0;

    private static int getErrorId() {
        return LAST_ERROR_ID++;
    }

    public static ErrorSchema badCredentialsError() {
        return ErrorSchema.builder()
                .id(getErrorId())
                .message("Bad credentials")
                .build();
    }

    public static ErrorSchema unauthorizedError() {
        return ErrorSchema.builder()
                .id(getErrorId())
                .message("Unauthorized error")
                .build();
    }

    public static ErrorSchema inputDataError() {
        return ErrorSchema.builder()
                .id(getErrorId())
                .message("Error input data")
                .build();
    }

    public static ErrorSchema deleteFileError() {
        return ErrorSchema.builder()
                .id(getErrorId())
                .message("Error delete file")
                .build();
    }

    public static ErrorSchema uploadFileError() {
        return ErrorSchema.builder()
                .id(getErrorId())
                .message("Error upload file")
                .build();
    }

    public static ErrorSchema gettingFileListError() {
        return ErrorSchema.builder()
                .id(getErrorId())
                .message("Error getting file list")
                .build();
    }

    public static ErrorSchema internalServerError() {
        return ErrorSchema.builder()
                .id(getErrorId())
                .message("Internal server error")
                .build();
    }
}