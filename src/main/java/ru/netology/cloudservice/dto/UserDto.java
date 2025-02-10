package ru.netology.cloudservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.netology.cloudservice.utils.Constants.CommonConstants;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String login;
    private String password;
    @JsonProperty(CommonConstants.AUTH_TOKEN)
    private String authToken;

    public UserDto(String authToken) {
        this.authToken = authToken;
    }
}