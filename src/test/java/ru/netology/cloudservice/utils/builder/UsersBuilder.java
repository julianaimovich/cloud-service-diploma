package ru.netology.cloudservice.utils.builder;

import ru.netology.cloudservice.model.UsersEntity;

public class UsersBuilder {

    public static UsersEntity getRandomUser() {
        return UsersEntity.builder()
                .login("login")
                .password("{noop}passwd")
                .build();
    }
}