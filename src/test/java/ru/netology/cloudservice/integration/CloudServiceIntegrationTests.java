package ru.netology.cloudservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.cloudservice.config.Constants;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.util.BaseConverter;
import ru.netology.cloudservice.util.TestConstants;
import ru.netology.cloudservice.util.builder.FileBuilder;
import ru.netology.cloudservice.util.builder.UserBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootTest
@Testcontainers
public class CloudServiceIntegrationTests {

    private static DockerComposeContainer<?> environment;

    private String sessionId;

    private String authToken;

    private static final Logger log;

    static {
        log = LoggerFactory.getLogger(CloudServiceIntegrationTests.class);
        log.info("Logger initialized for Rest Assured");

        // Устанавливаем глобальные фильтры логирования
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

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
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @BeforeEach
    public void login() throws JsonProcessingException {
        RestAssured.reset();
        UserDto userDto = UserBuilder.getExistentUserForRequest();
        Response login = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(BaseConverter.convertClassToJsonString(userDto))
                .when()
                .post(Constants.Endpoints.LOGIN)
                .then()
                .extract()
                .response();
        sessionId = login.getCookie(TestConstants.UserSessionValues.JSESSIONID);
        authToken = login.jsonPath().getString(TestConstants.UserSessionValues.AUTH_TOKEN);
    }

    @AfterAll
    public static void tearDown() {
        environment.stop();
    }

    @Test
    @DisplayName("Successful \".jpg\" file uploading")
    public void successfulJpgFileUploading() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();

        RestAssured.given()
                .log().all()
                .cookie(TestConstants.UserSessionValues.JSESSIONID, sessionId)
                .header(TestConstants.UserSessionValues.AUTH_TOKEN, authToken)
                .param(TestConstants.FilesParamValues.FILENAME_PARAM, fileForRequest.getFilename())
                .multiPart(TestConstants.FilesParamValues.FILE_PARAM, fileForRequest.getFile())
                .when()
                .post(Constants.Endpoints.FILE)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Successful \".txt\" file uploading")
    public void successfulTxtFileUploading() throws URISyntaxException, IOException {
        FileDto fileForRequest = FileBuilder.getTxtFileForRequest();
        MultipartFile fileContent = FileBuilder.fileToMultipartFile
                (fileForRequest.getFile(), fileForRequest.getContentType());

        RestAssured.given()
                .cookie(TestConstants.UserSessionValues.JSESSIONID, sessionId)
                .header(TestConstants.UserSessionValues.AUTH_TOKEN, authToken)
                .param(TestConstants.FilesParamValues.FILENAME_PARAM, fileForRequest.getFilename())
                .multiPart(TestConstants.FilesParamValues.FILE_PARAM, fileContent)
                .when()
                .post(Constants.Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Unable to upload file without filename")
    public void unableToUploadFileWithoutFilename() throws URISyntaxException, IOException {}

    @Test
    @DisplayName("Unable to upload file without file")
    public void unableToUploadFileWithoutFile() throws URISyntaxException, IOException {}
}