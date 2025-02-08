package ru.netology.cloudservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String login;
    private String password;
    @JsonProperty("auth-token")
    private String authToken;

    public UserDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserDto(String authToken) {
        this.authToken = authToken;
    }
}