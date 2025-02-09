package ru.netology.cloudservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.cloudservice.config.Constants.Endpoints;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.util.BaseConverter;
import ru.netology.cloudservice.util.TestConstants.UserSessionValues;
import ru.netology.cloudservice.util.builder.UserBuilder;

import java.io.File;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@Testcontainers
public class AuthorizationIntegrationTests {

    private static DockerComposeContainer<?> environment;

    @BeforeAll
    public static void setUp() {
        environment = new DockerComposeContainer<>(new File("docker-compose.yml"))
                .withLocalCompose(true);
        environment.start();

        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.socket.timeout", 10000)
                        .setParam("http.connection.timeout", 10000));
    }

    @AfterAll
    public static void tearDown() {
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
                .body(UserSessionValues.AUTH_TOKEN, notNullValue());
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

    @Test
    @DisplayName("Successful logout with a verified user's token")
    public void successfulLogoutWithVerifiedUsersTokenTest() throws JsonProcessingException {
        UserDto userDto = UserBuilder.getExistentUserForRequest();
        Response login = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(BaseConverter.convertClassToJsonString(userDto))
                .when()
                .post(Endpoints.LOGIN)
                .then()
                .extract()
                .response();

        String sessionId = login.getCookie(UserSessionValues.JSESSIONID);
        String authToken = login.jsonPath().getString(UserSessionValues.AUTH_TOKEN);

        RestAssured.given()
                .cookie(UserSessionValues.JSESSIONID, sessionId)
                .header(UserSessionValues.AUTH_TOKEN, authToken)
                .when()
                .post(Endpoints.LOGOUT)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Failed logout with non-existent user's token")
    public void failedLogoutWithNonExistentUsersTokenTest() {
        UserDto userDto = new UserDto(UUID.randomUUID().toString());
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userDto)
                .when()
                .post(Endpoints.LOGOUT)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Failed logout without token")
    public void failedLogoutWithoutTokenTest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(Endpoints.LOGOUT)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}