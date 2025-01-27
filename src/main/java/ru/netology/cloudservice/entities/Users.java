package ru.netology.cloudservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    private int id;
    private String login;
    private String password;
}