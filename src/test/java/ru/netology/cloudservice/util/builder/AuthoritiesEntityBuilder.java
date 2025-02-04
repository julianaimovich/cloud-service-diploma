package ru.netology.cloudservice.util.builder;

import ru.netology.cloudservice.model.AuthoritiesEntity;

import static ru.netology.cloudservice.util.TestConstants.RoleAuthorities.ROLE_ADMIN_AUTHORITY;

public class AuthoritiesEntityBuilder {

    public static AuthoritiesEntity getAdminAuthorityForUser(String login) {
        return AuthoritiesEntity.builder()
                .login(login)
                .authority(ROLE_ADMIN_AUTHORITY)
                .build();
    }
}