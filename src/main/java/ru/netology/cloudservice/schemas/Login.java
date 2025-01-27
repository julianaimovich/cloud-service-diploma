package ru.netology.cloudservice.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {
    private String login;
    private String password;
    @JsonProperty("auth-token")
    private String authToken;
}