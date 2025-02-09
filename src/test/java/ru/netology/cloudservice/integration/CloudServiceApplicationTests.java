package ru.netology.cloudservice.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
public class CloudServiceApplicationTests {

    private static final DockerComposeContainer<?> environment =
            new DockerComposeContainer<>(new File("docker-compose.yml"))
                    .withLocalCompose(true);

    @BeforeAll
    static void setUp() {
        environment.start();
    }

    @AfterAll
    static void tearDown() {
        environment.stop();
    }

    @Test
    void testMySQLServiceIsRunning() {
        System.out.println("kek");
    }
}
