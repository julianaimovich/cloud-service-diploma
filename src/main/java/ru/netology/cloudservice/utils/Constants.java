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
}