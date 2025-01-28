package ru.netology.cloudservice.schemas;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netology.cloudservice.entities.Users;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipalSchema implements UserDetails {
    private Users user;

    public UserPrincipalSchema(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }
}