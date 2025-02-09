package ru.netology.cloudservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.cloudservice.config.Constants.Endpoints;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.util.BaseConverter;
import ru.netology.cloudservice.util.ServerUtils;
import ru.netology.cloudservice.util.TestConstants.FilesParamValues;
import ru.netology.cloudservice.util.TestConstants.UserSessionValues;
import ru.netology.cloudservice.util.builder.BaseIntegrationTest;
import ru.netology.cloudservice.util.builder.FileBuilder;
import ru.netology.cloudservice.util.builder.UserBuilder;

import java.net.URISyntaxException;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@Testcontainers
public class AuthorizationIntegrationTests extends BaseIntegrationTest {

    private static final Logger log;

    static {
        log = LoggerFactory.getLogger(CloudServiceIntegrationTests.class);
        log.info("Logger initialized for Rest Assured");
        // Устанавливаем глобальные фильтры логирования
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @BeforeEach
    public void resetCookies() {
        ServerUtils.resetCookies();
    }

    @Test
    @DisplayName("Successful login with credentials of an existing user")
    public void successfulLoginWithCredentialsOfExistingUserTest() throws JsonProcessingException {
        UserDto userDto = UserBuilder.getExistentUserForRequest();
        String body = BaseConverter.convertClassToJsonString(userDto);
        RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post(Endpoints.LOGIN)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(UserSessionValues.AUTH_TOKEN, notNullValue());
    }

    @Test
    @DisplayName("Failed login with credentials of a non-existent user")
    public void failedLoginWithCredentialsOfNonExistentUserTest() throws JsonProcessingException {
        UserDto userDto = UserBuilder.getRandomUserForRequest();
        String body = BaseConverter.convertClassToJsonString(userDto);
        RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post(Endpoints.LOGIN)
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Successful logout with a verified user's token")
    public void successfulLogoutWithVerifiedUsersTokenTest() throws JsonProcessingException {
        Response login = ServerUtils.login();
        String sessionId = login.getDetailedCookie(UserSessionValues.JSESSIONID).getValue();
        String authToken = login.jsonPath().getString(UserSessionValues.AUTH_TOKEN);

        RestAssured.given()
                .log().all()
                .cookie(UserSessionValues.JSESSIONID, sessionId)
                .header(UserSessionValues.AUTH_TOKEN, authToken)
                .when()
                .post(Endpoints.LOGOUT)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Failed logout with non-existent user's token")
    public void failedLogoutWithNonExistentUsersTokenTest() {
        UserDto userDto = new UserDto(UUID.randomUUID().toString());
        RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userDto)
                .when()
                .post(Endpoints.LOGOUT)
                .then()
                .log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Failed logout without token")
    public void failedLogoutWithoutTokenTest() {
        RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(Endpoints.LOGOUT)
                .then()
                .log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("User can't get list of files without authorization")
    public void userCanNotGetListOfFilesWithoutAuthorizationTest() {
        RestAssured.given()
                .when()
                .log().all()
                .get(Endpoints.GET_ALL_FILES)
                .then()
                .log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("User can't upload file without authorization")
    public void userCanNotUploadFileWithoutAuthorizationTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();
        RestAssured.given()
                .log().all()
                .queryParam(FilesParamValues.FILENAME_PARAM, fileForRequest.getFilename())
                .multiPart(FilesParamValues.FILE_PARAM, fileForRequest.getFile())
                .when()
                .post(Endpoints.FILE)
                .then()
                .log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}