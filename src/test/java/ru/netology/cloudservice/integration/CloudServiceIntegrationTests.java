package ru.netology.cloudservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.netology.cloudservice.config.Constants.Endpoints;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.model.FilesEntity;
import ru.netology.cloudservice.repository.FilesRepository;
import ru.netology.cloudservice.util.ServerUtils;
import ru.netology.cloudservice.util.TestConstants.FilesParamValues;
import ru.netology.cloudservice.util.builder.BaseIntegrationTest;
import ru.netology.cloudservice.util.builder.FileBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static ru.netology.cloudservice.util.TestConstants.FilesParamValues.FILENAME_PARAM;
import static ru.netology.cloudservice.util.TestConstants.FilesParamValues.FILE_PARAM;
import static ru.netology.cloudservice.util.TestConstants.ServerParams.CACHE_CONTROL_HEADER;
import static ru.netology.cloudservice.util.TestConstants.ServerParams.CACHE_CONTROL_VALUE;
import static ru.netology.cloudservice.util.TestConstants.UserSessionValues.AUTH_TOKEN;
import static ru.netology.cloudservice.util.TestConstants.UserSessionValues.JSESSIONID;

public class CloudServiceIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private FilesRepository filesRepository;

    private String sessionId;

    private String authToken;

    @BeforeEach
    public void authorize() throws JsonProcessingException {
        ServerUtils.resetCookies();
        Response login = ServerUtils.login();
        sessionId = login.getCookie(JSESSIONID);
        authToken = login.jsonPath().getString(AUTH_TOKEN);
    }

    @Test
    @DisplayName("Successful \".jpg\" file uploading")
    public void successfulJpgFileUploadingTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .queryParam(FILENAME_PARAM, fileForRequest.getFilename())
                .multiPart(FILE_PARAM, fileForRequest.getFile())
                .when()
                .post(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.OK.value());
        filesRepository.deleteByFilename(fileForRequest.getFilename());
    }

    @Test
    @DisplayName("Successful \".txt\" file uploading")
    public void successfulTxtFileUploadingTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getTxtFileForRequest();
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .queryParam(FILENAME_PARAM, fileForRequest.getFilename())
                .multiPart(FILE_PARAM, fileForRequest.getFile())
                .when()
                .post(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.OK.value());
        filesRepository.deleteByFilename(fileForRequest.getFilename());
    }

    @Test
    @DisplayName("Unable to upload file without filename")
    public void unableToUploadFileWithoutFilenameTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .multiPart(FILE_PARAM, fileForRequest.getFile())
                .when()
                .post(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Unable to upload file without file content")
    public void unableToUploadFileWithoutFileContentTest() throws URISyntaxException {
        FileDto fileForRequest = FileBuilder.getTxtFileForRequest();
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .queryParam(FILENAME_PARAM, fileForRequest.getFilename())
                .when()
                .post(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Successful file downloading")
    public void successfulFileDownloadingTest() throws URISyntaxException, IOException {
        FilesEntity fileForRequest = filesRepository.save(FileBuilder.getJpgFileEntity());
        byte[] expectedFileContent = fileForRequest.getData();
        byte[] actualFileContent = RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .queryParam(FILENAME_PARAM, fileForRequest.getFilename())
                .when()
                .get(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .asByteArray();
        assertArrayEquals(expectedFileContent, actualFileContent);
        filesRepository.deleteById(fileForRequest.getId());
    }

    @Test
    @DisplayName("Unable to download file by non-existent filename")
    public void unableToDownloadFileByNonExistentFilenameTest() {
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .queryParam(FILENAME_PARAM, FileBuilder.faker.file().fileName())
                .when()
                .get(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Successful file editing")
    public void successfulFileEditingTest() throws IOException, URISyntaxException {
        FilesEntity existingFile = filesRepository.save(FileBuilder.getJpgFileEntity());
        FileDto fileForRequest = new FileDto(FilesParamValues.EDIT_FILE_NAME);
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(FILENAME_PARAM, existingFile.getFilename())
                .body(fileForRequest)
                .when()
                .put(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.OK.value());
        filesRepository.deleteById(existingFile.getId());
    }

    @Test
    @DisplayName("Unable to edit file by non-existent filename")
    public void unableToEditFileByNonExistentFilenameTest() throws IOException, URISyntaxException {
        FilesEntity fileEntity = FileBuilder.getJpgFileEntity();
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(FILENAME_PARAM, fileEntity.getFilename())
                .body(FileBuilder.faker.file().fileName())
                .when()
                .put(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Successful file deleting")
    public void successfulFileDeletingTest() throws IOException, URISyntaxException {
        FilesEntity fileForRequest = filesRepository.save(FileBuilder.getJpgFileEntity());
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .queryParam(FILENAME_PARAM, fileForRequest.getFilename())
                .when()
                .delete(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Unable to delete by non-existent filename")
    public void unableToDeleteFileByNonExistentFilenameTest() {
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                /*.contentType(MediaType.APPLICATION_JSON_VALUE)*/
                .queryParam(FILENAME_PARAM, FileBuilder.faker.file().fileName())
                .when()
                .delete(Endpoints.FILE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Get all existing files")
    public void getAllExistingFilesTest() throws IOException, URISyntaxException {
        List<FilesEntity> filesList = filesRepository.saveAll(FileBuilder.getFilesEntityList());
        List<Long> filesIds = filesList.stream().map(FilesEntity::getId).toList();
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .when()
                .get(Endpoints.GET_ALL_FILES)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(filesList.size()));
        filesRepository.deleteAllById(filesIds);
    }

    @Test
    @DisplayName("Get files by limit")
    public void getFilesByLimitTest() throws IOException, URISyntaxException {
        List<FilesEntity> filesList = filesRepository.saveAll(FileBuilder.getFilesEntityList());
        List<Long> filesIds = filesList.stream().map(FilesEntity::getId).toList();
        Integer limit = FileBuilder.faker.number().numberBetween(1, filesList.size() - 1);
        List<FilesEntity> expectedFilesList = filesList.subList(0, limit);
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                /*.contentType(MediaType.APPLICATION_JSON_VALUE)*/
                .queryParam(FilesParamValues.LIMIT_PARAM, limit)
                .when()
                .get(Endpoints.GET_ALL_FILES)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(expectedFilesList.size()));
        filesRepository.deleteAllById(filesIds);
    }

    @Test
    @DisplayName("Unable to get files by limit less than zero")
    public void unableToGetFilesByLimitLessThanZeroTest() {
        Integer limit = FileBuilder.faker.number().negative();
        RestAssured.given()
                .cookie(JSESSIONID, sessionId)
                .header(AUTH_TOKEN, authToken)
                .header(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(FilesParamValues.LIMIT_PARAM, limit)
                .when()
                .get(Endpoints.GET_ALL_FILES)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}