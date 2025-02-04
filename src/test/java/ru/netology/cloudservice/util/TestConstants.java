package ru.netology.cloudservice.util;

public interface TestConstants {

    interface RoleAuthorities {
        String ROLE_ADMIN_AUTHORITY = "ROLE_ADMIN";
        String ROLE_USER_AUTHORITY = "ROLE_USER";
    }

    interface FilesParamValues {
        String FILE_JPG_PATH = "testFiles/popug.jpg";
        String FILE_TXT_PATH = "testFiles/text.txt";
        String IMAGE_CONTENT_TYPE = "image/jpeg";
        String TEXT_CONTENT_TYPE = "text/plain";
    }

    interface ExceptionMessages {
        String NOT_NULL_PROPERTY_NULL_REFERENCE = "not-null property references a null or transient value";
    }
}