package ru.netology.cloudservice.api.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class LoginSchema extends BaseSchema {
    private String login;
    private String password;
    @JsonProperty("auth-token")
    private String authToken;

    public LoginSchema(String authToken) {
        this.authToken = authToken;
    }
}