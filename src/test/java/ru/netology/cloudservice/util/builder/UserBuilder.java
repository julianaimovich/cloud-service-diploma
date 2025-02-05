package ru.netology.cloudservice.util.builder;

import net.datafaker.Faker;
import ru.netology.cloudservice.model.UsersEntity;

public class UserBuilder {

    public static final Faker faker = new Faker();

    public static UsersEntity getRandomUserEntity() {
        return UsersEntity.builder()
                .login(faker.internet().username())
                .password("{noop}" + faker.internet().password())
                .build();
    }
}