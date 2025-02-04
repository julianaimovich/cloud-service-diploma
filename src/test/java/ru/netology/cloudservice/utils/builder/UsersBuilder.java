package ru.netology.cloudservice.utils.builder;

import net.datafaker.Faker;
import ru.netology.cloudservice.model.UsersEntity;

public class UsersBuilder {

    private static final Faker faker = new Faker();

    public static UsersEntity getRandomUser() {
        return UsersEntity.builder()
                .login(faker.internet().username())
                .password("{noop}" + faker.internet().password())
                .build();
    }
}