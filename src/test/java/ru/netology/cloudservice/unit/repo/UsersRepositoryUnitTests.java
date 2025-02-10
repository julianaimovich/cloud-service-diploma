package ru.netology.cloudservice.unit.repo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import ru.netology.cloudservice.model.UsersEntity;
import ru.netology.cloudservice.repository.UsersRepository;
import ru.netology.cloudservice.util.TestConstants.ExceptionMessages;
import ru.netology.cloudservice.util.builder.UserBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class UsersRepositoryUnitTests {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @Rollback
    @DisplayName("Save user")
    public void saveUserTest() {
        // Act
        UsersEntity user = usersRepository.save(UserBuilder.getRandomUserEntity());
        List<UsersEntity> allUsersInSystem = usersRepository.findAll();
        // Assert
        assertTrue(allUsersInSystem.contains(user));
    }

    @Test
    @DisplayName("Unable to save user without login")
    public void unableToSaveUserWithoutLoginTest() {
        // Arrange
        UsersEntity user = UserBuilder.getRandomUserEntity();
        user.setLogin(null);
        // Act
        DataIntegrityViolationException exception = assertThrows
                (DataIntegrityViolationException.class, () -> usersRepository.save(user));
        // Assert
        assertTrue(exception.getMessage().contains(ExceptionMessages.NOT_NULL_PROPERTY_NULL_REFERENCE));
    }

    @Test
    @DisplayName("Unable to save user without password")
    public void unableToSaveUserWithoutPasswordTest() {
        // Arrange
        UsersEntity user = UserBuilder.getRandomUserEntity();
        user.setPassword(null);
        // Act
        DataIntegrityViolationException exception = assertThrows
                (DataIntegrityViolationException.class, () -> usersRepository.save(user));
        // Assert
        assertTrue(exception.getMessage().contains(ExceptionMessages.NOT_NULL_PROPERTY_NULL_REFERENCE));
    }

    @Test
    @Rollback
    @DisplayName("Get user by id")
    public void getUserByIdTest() {
        // Arrange
        UsersEntity user = usersRepository.save(UserBuilder.getRandomUserEntity());
        // Act
        UsersEntity userFromDb = usersRepository.findById(user.getId()).orElseThrow();
        // Assert
        assertEquals(user, userFromDb);
    }

    @Test
    @Rollback
    @DisplayName("Get user by login")
    public void getUserByLoginTest() {
        // Arrange
        UsersEntity user = usersRepository.save(UserBuilder.getRandomUserEntity());
        // Act
        UsersEntity userFromDb = usersRepository.findByLogin(user.getLogin()).orElseThrow();
        // Assert
        assertEquals(user, userFromDb);
    }

    @Test
    @DisplayName("Get all existing users")
    public void getAllUsersTest() {
        // Act
        List<UsersEntity> usersList = usersRepository.findAll();
        // Assert
        assertThat(usersList.size()).isGreaterThan(0);
    }

    @Test
    @Rollback
    @DisplayName("Update user")
    public void updateUserTest() {
        // Arrange
        UsersEntity user = usersRepository.save(UserBuilder.getRandomUserEntity());
        user.setLogin(UserBuilder.faker.internet().username());
        // Act
        UsersEntity userUpdated =  usersRepository.save(user);
        // Assert
        assertEquals(user.getLogin(), userUpdated.getLogin());
    }

    @Test
    @DisplayName("Delete user")
    public void deleteUserTest() {
        // Arrange
        UsersEntity user = usersRepository.save(UserBuilder.getRandomUserEntity());
        // Act
        usersRepository.delete(user);
        Optional<UsersEntity> optional = usersRepository.findById(user.getId());
        // Assert
        assertTrue(optional.isEmpty());
    }
}