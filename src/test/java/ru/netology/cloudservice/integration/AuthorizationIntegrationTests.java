package ru.netology.cloudservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.netology.cloudservice.config.Constants.Endpoints;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.util.BaseConverter;
import ru.netology.cloudservice.util.ServerUtils;
import ru.netology.cloudservice.util.TestConstants.UserSessionValues;
import ru.netology.cloudservice.util.builder.FileBuilder;
import ru.netology.cloudservice.util.builder.UserBuilder;

import java.net.URISyntaxException;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static ru.netology.cloudservice.util.TestConstants.FilesParamValues.FILENAME_PARAM;
import static ru.netology.cloudservice.util.TestConstants.FilesParamValues.FILE_PARAM;
import static ru.netology.cloudservice.util.TestConstants.ServerParams.CACHE_CONTROL_HEADER;
import static ru.netology.cloudservice.util.TestConstants.ServerParams.CACHE_CONTROL_VALUE;

public class AuthorizationIntegrationTests extends BaseIntegrationTest {

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
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
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
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
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
        Response login = ServerUtils.login();
        String sessionId = login.getDetailedCookie(UserSessionValues.JSESSIONID).getValue();
        String authToken = login.jsonPath().getString(UserSessionValues.AUTH_TOKEN);
        RestAssured.given()
                .cookie(UserSessionValues.JSESSIONID, sessionId)
                .header(UserSessionValues.AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
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
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
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
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(Endpoints.LOGOUT)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("User can't get list of files without authorization")
    public void userCanNotGetListOfFilesWithoutAuthorizationTest() {
        RestAssured.given()
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .when()
                .get(Endpoints.GET_ALL_FILES)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("User can't upload file without authorization")
    public void userCanNotUploadFileWithoutAuthorizationTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();
        RestAssured.given()
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .queryParam(FILENAME_PARAM, fileForRequest.getFilename())
                .multiPart(FILE_PARAM, fileForRequest.getFile())
                .when()
                .post(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}