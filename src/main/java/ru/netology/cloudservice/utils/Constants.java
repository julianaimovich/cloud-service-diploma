package ru.netology.cloudservice.utils;

public interface Constants {

    interface CommonConstants {
        String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
        String AUTH_TOKEN = "auth-token";
    }

    interface Endpoints {
        String LOGIN = "/login";
        String LOGOUT = "/logout";
        String GET_ALL_FILES = "/list";
        String FILE = "/file";
    }

    interface ErrorMessages {
        String BAD_CREDENTIALS = "Bad credentials";
        String ERROR_GETTING_FILE_LIST = "Error getting file list";
        String ERROR_INPUT_DATA = "Error input data";
        String ERROR_UPLOAD_FILE = "Error upload file";
        String ERROR_DOWNLOAD_FILE = "Error download file";
        String ERROR_DELETE_FILE = "Error delete file";
        String SESSION_NOT_FOUND = "Session not found";
        String INVALID_AUTH_TOKEN = "Invalid auth token";
    }
}