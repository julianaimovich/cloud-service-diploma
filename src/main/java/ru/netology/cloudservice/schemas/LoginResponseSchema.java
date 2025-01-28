package ru.netology.cloudservice.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginResponseSchema extends ResponseSchema {
    @JsonProperty("auth-token")
    private String authToken;
}