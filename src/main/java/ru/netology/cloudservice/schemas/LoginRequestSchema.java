package ru.netology.cloudservice.schemas;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestSchema {
    private String login;
    private String password;
}