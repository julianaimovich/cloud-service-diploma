package ru.netology.cloudservice.repo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import ru.netology.cloudservice.model.UsersEntity;
import ru.netology.cloudservice.repository.UsersRepository;
import ru.netology.cloudservice.utils.builder.UsersBuilder;

import java.util.List;

@DataJpaTest
@ActiveProfiles({"test"})
public class UsersRepositoryUnitTests {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("Save user test")
    @Rollback()
    public void saveUserTest() {
        UsersEntity user = usersRepository.save(UsersBuilder.getRandomUser());
        List<UsersEntity> allUsersInSystem = usersRepository.findAll();
        Assertions.assertTrue(allUsersInSystem.contains(user));
    }
}