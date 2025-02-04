package ru.netology.cloudservice.repo;

import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import ru.netology.cloudservice.model.UsersEntity;
import ru.netology.cloudservice.repository.UsersRepository;
import ru.netology.cloudservice.utils.ExceptionMessages;
import ru.netology.cloudservice.utils.builder.UsersBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles({"test"})
public class UsersRepositoryUnitTests {

    @Autowired
    private UsersRepository usersRepository;
    private static final Faker faker = new Faker();

    @Test
    @Rollback()
    @DisplayName("Save user")
    public void saveUserTest() {
        UsersEntity user = usersRepository.save(UsersBuilder.getRandomUser());
        List<UsersEntity> allUsersInSystem = usersRepository.findAll();
        assertTrue(allUsersInSystem.contains(user));
    }

    @Test
    @DisplayName("Unable to save user without login")
    public void unableToSaveUserWithoutLoginTest() {
        UsersEntity user = UsersBuilder.getRandomUser();
        user.setLogin(null);
        DataIntegrityViolationException exception = assertThrows
                (DataIntegrityViolationException.class, () -> usersRepository.save(user));
        assertTrue(exception.getMessage().contains(ExceptionMessages.notNullPropertyReferencesNull()));
    }

    @Test
    @DisplayName("Unable to save user without password")
    public void unableToSaveUserWithoutPasswordTest() {
        UsersEntity user = UsersBuilder.getRandomUser();
        user.setPassword(null);
        DataIntegrityViolationException exception = assertThrows
                (DataIntegrityViolationException.class, () -> usersRepository.save(user));
        assertTrue(exception.getMessage().contains(ExceptionMessages.notNullPropertyReferencesNull()));
    }

    @Test
    @Rollback()
    @DisplayName("Get user by id")
    public void getUserByIdTest() {
        UsersEntity user = usersRepository.save(UsersBuilder.getRandomUser());
        UsersEntity userFromDb = usersRepository.findById(user.getId()).orElseThrow();
        assertEquals(user, userFromDb);
    }

    @Test
    @Rollback()
    @DisplayName("Get user by login")
    public void getUserByLoginTest() {
        UsersEntity user = usersRepository.save(UsersBuilder.getRandomUser());
        UsersEntity userFromDb = usersRepository.findByLogin(user.getLogin()).orElseThrow();
        assertEquals(user, userFromDb);
    }

    @Test
    @DisplayName("Get all existing users")
    public void getAllUsersTest() {
        List<UsersEntity> usersList = usersRepository.findAll();
        assertThat(usersList.size()).isGreaterThan(0);
    }

    @Test
    @Rollback()
    @DisplayName("Update user")
    public void updateUserTest() {
        UsersEntity user = usersRepository.save(UsersBuilder.getRandomUser());
        user.setLogin(faker.internet().username());
        UsersEntity userUpdated =  usersRepository.save(user);
        assertEquals(user.getLogin(), userUpdated.getLogin());
    }

    @Test
    @DisplayName("Delete user")
    public void deleteUserTest() {
        UsersEntity user = usersRepository.save(UsersBuilder.getRandomUser());
        usersRepository.delete(user);
        Optional<UsersEntity> optional = usersRepository.findById(user.getId());
        assertTrue(optional.isEmpty());
    }
}