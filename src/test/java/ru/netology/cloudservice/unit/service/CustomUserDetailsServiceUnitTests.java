package ru.netology.cloudservice.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netology.cloudservice.model.AuthoritiesEntity;
import ru.netology.cloudservice.model.UsersEntity;
import ru.netology.cloudservice.repository.AuthoritiesRepository;
import ru.netology.cloudservice.repository.UsersRepository;
import ru.netology.cloudservice.service.CustomUserDetailsService;
import ru.netology.cloudservice.util.builder.AuthoritiesBuilder;
import ru.netology.cloudservice.util.builder.UserBuilder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceUnitTests {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private AuthoritiesRepository authoritiesRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    @DisplayName("Load user by login")
    public void loadUserByLoginTest() {
        // Given
        UsersEntity expectedUser = UserBuilder.getRandomUserEntity();
        when(usersRepository.findByLogin(expectedUser.getLogin())).thenReturn(Optional.of(expectedUser));
        // When
        UserDetails result = userDetailsService.loadUserByUsername(expectedUser.getLogin());
        // Then
        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(usersRepository, Mockito.times(1)).findByLogin(expectedUser.getLogin());
    }

    @Test
    @DisplayName("Get user authorities by login")
    public void getAuthoritiesByLoginTest() {
        // Given
        UsersEntity user = UserBuilder.getRandomUserEntity();
        AuthoritiesEntity authority = AuthoritiesBuilder.getAdminAuthorityForUser(user.getLogin());
        List<GrantedAuthority> expectedAuthorities = AuthoritiesBuilder
                .getUserGrantedAuthorities(authority);
        when(authoritiesRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(authority));
        // When
        List<GrantedAuthority> result = userDetailsService.getAuthorities(user.getLogin());
        // Then
        assertNotNull(result);
        assertEquals(expectedAuthorities, result);
        verify(authoritiesRepository, Mockito.times(1)).findByLogin(user.getLogin());
    }
}