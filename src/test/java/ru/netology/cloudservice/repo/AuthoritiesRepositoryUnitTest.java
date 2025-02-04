package ru.netology.cloudservice.repo;

import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import ru.netology.cloudservice.model.AuthoritiesEntity;
import ru.netology.cloudservice.model.UsersEntity;
import ru.netology.cloudservice.repository.AuthoritiesRepository;
import ru.netology.cloudservice.repository.UsersRepository;
import ru.netology.cloudservice.util.TestConstants;
import ru.netology.cloudservice.util.TestConstants.ExceptionMessages;
import ru.netology.cloudservice.util.builder.AuthoritiesEntityBuilder;
import ru.netology.cloudservice.util.builder.UsersEntityBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles({"test"})
public class AuthoritiesRepositoryUnitTest {

    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    @Autowired
    private UsersRepository usersRepository;
    private final Faker faker = new Faker();

    @Test
    @Rollback
    @DisplayName("Save user authority")
    public void saveAuthorityTest() {
        // Arrange
        UsersEntity user = usersRepository.save(UsersEntityBuilder.getRandomUser());
        // Act
        AuthoritiesEntity authority = authoritiesRepository.save
                (AuthoritiesEntityBuilder.getAdminAuthorityForUser(user.getLogin()));
        List<AuthoritiesEntity> allAuthoritiesInSystem = authoritiesRepository.findAll();
        // Assert
        assertTrue(allAuthoritiesInSystem.contains(authority));
    }

    @Test
    @DisplayName("Unable to save authority without user login")
    public void unableToSaveAuthorityWithoutLoginTest() {
        // Arrange
        AuthoritiesEntity authority = AuthoritiesEntity.builder()
                .authority(TestConstants.RoleAuthorities.ROLE_ADMIN_AUTHORITY).build();
        // Act
        DataIntegrityViolationException exception = assertThrows
                (DataIntegrityViolationException.class, () -> authoritiesRepository.save(authority));
        // Assert
        assertTrue(exception.getMessage().contains(ExceptionMessages.NOT_NULL_PROPERTY_NULL_REFERENCE));
    }

    @Test
    @DisplayName("Unable to save authority without user role")
    public void unableToSaveAuthorityWithoutRoleTest() {
        // Arrange
        AuthoritiesEntity authority = AuthoritiesEntity.builder()
                .login(faker.internet().username()).build();
        // Act
        DataIntegrityViolationException exception = assertThrows
                (DataIntegrityViolationException.class, () -> authoritiesRepository.save(authority));
        // Assert
        assertTrue(exception.getMessage().contains(ExceptionMessages.NOT_NULL_PROPERTY_NULL_REFERENCE));
    }

    @Test
    @Rollback
    @DisplayName("Get user authority by id")
    public void getAuthorityByIdTest() {
        // Arrange
        UsersEntity user = usersRepository.save(UsersEntityBuilder.getRandomUser());
        AuthoritiesEntity authority = authoritiesRepository.save
                (AuthoritiesEntityBuilder.getAdminAuthorityForUser(user.getLogin()));
        // Act
        AuthoritiesEntity authorityFromDb = authoritiesRepository.findById
                (authority.getId()).orElseThrow();
        // Assert
        assertEquals(authority, authorityFromDb);
    }

    @Test
    @Rollback
    @DisplayName("Get authority by user login")
    public void getAuthorityByLoginTest() {
        // Arrange
        UsersEntity user = usersRepository.save(UsersEntityBuilder.getRandomUser());
        AuthoritiesEntity authority = authoritiesRepository.save
                (AuthoritiesEntityBuilder.getAdminAuthorityForUser(user.getLogin()));
        // Act
        AuthoritiesEntity authorityFromDb = authoritiesRepository.findByLogin
                (user.getLogin()).orElseThrow();
        // Assert
        assertEquals(authority, authorityFromDb);
    }

    @Test
    @DisplayName("Get all existing user authorities")
    public void getAllExistingAuthoritiesTest() {
        // Act
        List<AuthoritiesEntity> authoritiesList = authoritiesRepository.findAll();
        // Assert
        assertThat(authoritiesList.size()).isGreaterThan(0);
    }

    @Test
    @Rollback
    @DisplayName("Update user authority")
    public void updateAuthorityTest() {
        // Arrange
        UsersEntity user = usersRepository.save(UsersEntityBuilder.getRandomUser());
        AuthoritiesEntity authority = authoritiesRepository.save
                (AuthoritiesEntityBuilder.getAdminAuthorityForUser(user.getLogin()));
        authority.setAuthority(TestConstants.RoleAuthorities.ROLE_USER_AUTHORITY);
        // Act
        AuthoritiesEntity authorityUpdated =  authoritiesRepository.save(authority);
        // Assert
        assertEquals(authority.getAuthority(), authorityUpdated.getAuthority());
    }

    @Test
    @Rollback
    @DisplayName("Delete user authority")
    public void deleteAuthorityTest() {
        // Arrange
        UsersEntity user = usersRepository.save(UsersEntityBuilder.getRandomUser());
        AuthoritiesEntity authority = authoritiesRepository.save
                (AuthoritiesEntityBuilder.getAdminAuthorityForUser(user.getLogin()));
        // Act
        authoritiesRepository.delete(authority);
        Optional<AuthoritiesEntity> optional = authoritiesRepository.findById(authority.getId());
        // Assert
        assertTrue(optional.isEmpty());
    }
}