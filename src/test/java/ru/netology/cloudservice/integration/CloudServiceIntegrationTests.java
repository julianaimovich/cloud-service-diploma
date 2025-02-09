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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.cloudservice.config.Constants.Endpoints;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.service.FilesService;
import ru.netology.cloudservice.util.ServerUtils;
import ru.netology.cloudservice.util.TestConstants;
import ru.netology.cloudservice.util.builder.BaseIntegrationTest;
import ru.netology.cloudservice.util.builder.FileBuilder;

import java.net.URISyntaxException;

@SpringBootTest
@Testcontainers
public class CloudServiceIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private FilesService filesService;

    private static String sessionId;

    private static String authToken;

    private static final Logger log;

    static {
        log = LoggerFactory.getLogger(CloudServiceIntegrationTests.class);
        log.info("Logger initialized for Rest Assured");
        // Устанавливаем глобальные фильтры логирования
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @BeforeEach
    public void authorize() throws JsonProcessingException {
        ServerUtils.resetCookies();
        Response login = ServerUtils.login();
        sessionId = login.getCookie(TestConstants.UserSessionValues.JSESSIONID);
        authToken = login.jsonPath().getString(TestConstants.UserSessionValues.AUTH_TOKEN);
    }

    @Test
    @DisplayName("Successful \".jpg\" file uploading")
    public void successfulJpgFileUploadingTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();

        RestAssured.given()
                .log().all()
                .cookie(TestConstants.UserSessionValues.JSESSIONID, sessionId)
                .header(TestConstants.UserSessionValues.AUTH_TOKEN, authToken)
                .queryParam(TestConstants.FilesParamValues.FILENAME_PARAM, fileForRequest.getFilename())
                .multiPart(TestConstants.FilesParamValues.FILE_PARAM, fileForRequest.getFile())
                .when()
                .post(Endpoints.FILE)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Successful \".txt\" file uploading")
    public void successfulTxtFileUploadingTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getTxtFileForRequest();

        RestAssured.given()
                .log().all()
                .cookie(TestConstants.UserSessionValues.JSESSIONID, sessionId)
                .header(TestConstants.UserSessionValues.AUTH_TOKEN, authToken)
                .queryParam(TestConstants.FilesParamValues.FILENAME_PARAM, fileForRequest.getFilename())
                .multiPart(TestConstants.FilesParamValues.FILE_PARAM, fileForRequest.getFile())
                .when()
                .post(Endpoints.FILE)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Unable to upload file without filename")
    public void unableToUploadFileWithoutFilenameTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();

        RestAssured.given()
                .log().all()
                .cookie(TestConstants.UserSessionValues.JSESSIONID, sessionId)
                .header(TestConstants.UserSessionValues.AUTH_TOKEN, authToken)
                .multiPart(TestConstants.FilesParamValues.FILE_PARAM, fileForRequest.getFile())
                .when()
                .post(Endpoints.FILE)
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Unable to upload file without file content")
    public void unableToUploadFileWithoutFileContentTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getTxtFileForRequest();

        RestAssured.given()
                .log().all()
                .cookie(TestConstants.UserSessionValues.JSESSIONID, sessionId)
                .header(TestConstants.UserSessionValues.AUTH_TOKEN, authToken)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .queryParam(TestConstants.FilesParamValues.FILENAME_PARAM, fileForRequest.getFilename())
                .when()
                .post(Endpoints.FILE)
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /*@Test
    @DisplayName("Successful file downloading")
    public void successfulFileDownloadingTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();

        RestAssured.given()
                .cookie(TestConstants.UserSessionValues.JSESSIONID, sessionId)
                .header(TestConstants.UserSessionValues.AUTH_TOKEN, authToken)
                .param(TestConstants.FilesParamValues.FILENAME_PARAM, fileForRequest.getFilename())
                .multiPart(TestConstants.FilesParamValues.FILE_PARAM, fileForRequest.getFile())
                .when()
                .post(Constants.Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.OK.value());
    }*/
}