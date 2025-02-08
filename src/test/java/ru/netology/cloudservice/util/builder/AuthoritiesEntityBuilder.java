package ru.netology.cloudservice.util.builder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.netology.cloudservice.model.AuthoritiesEntity;

import java.util.ArrayList;
import java.util.List;

import static ru.netology.cloudservice.util.TestConstants.UserParamValues.ROLE_ADMIN_AUTHORITY;

public class AuthoritiesEntityBuilder {

    public static AuthoritiesEntity getAdminAuthorityForUser(String login) {
        return AuthoritiesEntity.builder()
                .login(login)
                .authority(ROLE_ADMIN_AUTHORITY)
                .build();
    }

    public static List<GrantedAuthority> getUserGrantedAuthorities(AuthoritiesEntity authoritiesEntity) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(authoritiesEntity.getAuthority()));
        return authorities;
    }
}