package ru.netology.cloudservice.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.netology.cloudservice.controller.FilesController;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.model.FilesEntity;
import ru.netology.cloudservice.service.FilesService;
import ru.netology.cloudservice.util.TestConstants.FilesParamValues;
import ru.netology.cloudservice.util.builder.FileBuilder;
import ru.netology.cloudservice.utils.Constants.Endpoints;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.netology.cloudservice.util.TestConstants.FilesParamValues.FILENAME_PARAM;
import static ru.netology.cloudservice.util.TestConstants.FilesParamValues.LIMIT_PARAM;

@WebMvcTest(FilesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FilesControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FilesService filesService;

    @Test
    @DisplayName("Upload file")
    public void uploadFileTest() throws Exception {
        // Given
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();
        MockMultipartFile fileContent = FileBuilder.fileToMultipartFile
                (fileForRequest.getFile(), MediaType.IMAGE_JPEG_VALUE);
        FilesEntity expectedEntity = FileBuilder.fileDtoToEntity(fileForRequest);
        when(filesService.saveFile(fileForRequest.getFilename(), fileContent))
                .thenReturn(expectedEntity);
        // When
        ResultActions response = mockMvc.perform(
                multipart(Endpoints.FILE).file(fileContent)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .queryParam(FILENAME_PARAM, fileForRequest.getFilename()));
        // Then
        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Download file")
    public void downloadFileTest() throws Exception {
        // Given
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();
        byte[] fileContent = FileUtils.readFileToByteArray(fileForRequest.getFile());
        when(filesService.getFile(fileForRequest.getFilename())).thenReturn(fileContent);
        when(filesService.getFileContentType(fileForRequest.getFilename()))
                .thenReturn(fileForRequest.getContentType());
        // When
        ResultActions response = mockMvc.perform(get(Endpoints.FILE)
                .queryParam(FILENAME_PARAM, fileForRequest.getFilename())
                .contentType(fileForRequest.getContentType()));
        // Then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(fileForRequest.getContentType()))
                .andExpect(content().bytes(fileContent));
    }

    @Test
    @DisplayName("Edit file")
    public void editFileTest() throws Exception {
        // Given
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();
        FileDto fileForResponse = new FileDto(FilesParamValues.EDIT_FILE_NAME);
        FilesEntity fileEntity = FileBuilder.fileDtoToEntity(fileForRequest);
        when(filesService.editFile(fileForRequest.getFilename(), FilesParamValues.EDIT_FILE_NAME))
                .thenReturn(fileEntity);
        // When
        ResultActions response = mockMvc.perform(put(Endpoints.FILE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(FILENAME_PARAM, fileForRequest.getFilename())
                .content(objectMapper.writeValueAsString(fileForResponse)));
        // Then
        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete file")
    public void deleteFileTest() throws Exception {
        // Given
        FileDto fileForRequest = FileBuilder.getJpgFileForRequest();
        willDoNothing().given(filesService).deleteFile(fileForRequest.getFilename());
        // When
        ResultActions response = mockMvc.perform(delete(Endpoints.FILE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(FILENAME_PARAM, fileForRequest.getFilename()));
        // Then
        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get all files by limit")
    public void getAllFilesByLimitTest() throws Exception {
        // Given
        List<FileDto> filesList = FileBuilder.getFileDtoList();
        Integer limit = FileBuilder.faker.number().numberBetween(1, filesList.size() - 1);
        List<FileDto> expectedFilesList = filesList.subList(0, limit);
        when(filesService.getAllFilesByLimit(limit)).thenReturn(expectedFilesList);
        // When
        ResultActions response = mockMvc.perform(get(Endpoints.GET_ALL_FILES)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(LIMIT_PARAM, limit.toString()));
        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(limit)));
    }
}