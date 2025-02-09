package ru.netology.cloudservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.cloudservice.config.Constants.Endpoints;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.util.BaseConverter;
import ru.netology.cloudservice.util.TestConstants;
import ru.netology.cloudservice.util.builder.UserBuilder;

import java.io.File;

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@Testcontainers
public class AuthorizationIntegrationTests {

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    private static final DockerComposeContainer<?> environment =
            new DockerComposeContainer<>(new File("docker-compose.yml"))
                    .withLocalCompose(true);

    @BeforeAll
    static void setUp() {
        environment.start();
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.socket.timeout", 10000)
                        .setParam("http.connection.timeout", 10000));
    }

    @AfterAll
    static void tearDown() {
        environment.stop();
    }

    @Test
    @DisplayName("Successful login with credentials of an existing user")
    public void successfulLoginWithCredentialsOfExistingUserTest() throws JsonProcessingException {
        UserDto userDto = UserBuilder.getExistentUserForRequest();
        String body = BaseConverter.convertClassToJsonString(userDto);
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post(Endpoints.LOGIN)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(TestConstants.UserParamValues.AUTH_TOKEN_PARAM, notNullValue());
    }

    @Test
    @DisplayName("Failed login with credentials of a non-existent user")
    public void failedLoginWithCredentialsOfNonExistentUserTest() throws JsonProcessingException {
        UserDto userDto = UserBuilder.getRandomUserForRequest();
        String body = BaseConverter.convertClassToJsonString(userDto);
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post(Endpoints.LOGIN)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /*@Test
    @DisplayName("Log out")
    public void logOutTest() throws JsonProcessingException {}*/
}