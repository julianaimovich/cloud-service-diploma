package ru.netology.cloudservice.util;

public interface TestConstants {

    interface ServerParams {
        String SERVER_URI = "http://localhost:8080";
        String DOCKER_COMPOSE_FILE = "docker-compose.yml";
        String ACTUATOR_HEALTH_ENDPOINT = "/actuator/health";
        String CACHE_CONTROL_HEADER = "Cache-Control";
        String CACHE_CONTROL_VALUE = "no-cache";
    }

    interface UserSessionValues {
        String ROLE_ADMIN_AUTHORITY = "ROLE_ADMIN";
        String ROLE_USER_AUTHORITY = "ROLE_USER";
        String AUTH_TOKEN = "auth-token";
        String JSESSIONID = "JSESSIONID";
    }

    interface FilesParamValues {
        String FILE_JPG_PATH = "testFiles/parrot.jpg";
        String FILE_TXT_PATH = "testFiles/text.txt";
        String FILENAME_PARAM = "filename";
        String FILE_PARAM = "file";
        String EDIT_FILE_NAME = "kek.jpg";
        String LIMIT_PARAM = "limit";
        Long FILE_DEFAULT_ID = 100L;
    }

    interface ExceptionMessages {
        String NOT_NULL_PROPERTY_NULL_REFERENCE = "not-null property references a null or transient value";
    }
}