package ru.netology.cloudservice.util.builder;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.cloudservice.util.ServerUtils;
import ru.netology.cloudservice.util.TestConstants.ServerParams;

import java.io.File;

@SpringBootTest
@Testcontainers
public class BaseIntegrationTest {

    private static DockerComposeContainer<?> environment;

    @BeforeAll
    public static void setUp() throws InterruptedException {
        environment = new DockerComposeContainer<>(new File(ServerParams.DOCKER_COMPOSE_FILE))
                .withLocalCompose(true);
        environment.start();

        RestAssured.baseURI = ServerParams.SERVER_URI;
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.socket.timeout", 10000)
                        .setParam("http.connection.timeout", 10000));

        ServerUtils.waitForServer();
    }

    @AfterAll
    public static void tearDown() {
        environment.stop();
    }
}