package ru.netology.cloudservice.utils.builder;

import ru.netology.cloudservice.model.AuthoritiesEntity;

import static ru.netology.cloudservice.utils.TestConstants.RoleAuthorities.ROLE_ADMIN_AUTHORITY;

public class AuthoritiesBuilder {

    public static AuthoritiesEntity getAdminAuthorityForUser(String login) {
        return AuthoritiesEntity.builder()
                .login(login)
                .authority(ROLE_ADMIN_AUTHORITY)
                .build();
    }
}