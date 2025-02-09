package ru.netology.cloudservice.util.builder;

import net.datafaker.Faker;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.model.UsersEntity;

import java.util.UUID;

public class UserBuilder {

    public static final Faker faker = new Faker();

    public static UsersEntity getRandomUserEntity() {
        return UsersEntity.builder()
                .login(faker.internet().username())
                .password("{noop}" + faker.internet().password())
                .build();
    }

    public static UserDto getRandomUserForRequest() {
        return UserDto.builder()
                .login(faker.internet().username())
                .password(faker.internet().password())
                .build();
    }

    public static UserDto getExistentUserForRequest() {
        return UserDto.builder()
                .login("admin")
                .password("adminpasswd")
                .build();
    }

    public static UserDto getRandomAuthTokenForResponse() {
        return UserDto.builder()
                .authToken(UUID.randomUUID().toString())
                .build();
    }
}