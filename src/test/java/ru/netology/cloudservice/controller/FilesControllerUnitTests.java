package ru.netology.cloudservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.netology.cloudservice.config.Constants.Endpoints;
import ru.netology.cloudservice.dto.FileDto;
import ru.netology.cloudservice.model.FilesEntity;
import ru.netology.cloudservice.service.FilesService;
import ru.netology.cloudservice.util.TestConstants.FilesParamValues;
import ru.netology.cloudservice.util.builder.FileBuilder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FilesControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FilesService filesService;

    @Test
    @DisplayName("Upload file")
    public void uploadFileTest() throws Exception {
        // Given
        FileDto fileForRequest = FileBuilder.getFileForRequest();
        MockMultipartFile fileContent = FileBuilder.fileToMultipartFile
                (fileForRequest.getFile(), MediaType.IMAGE_JPEG_VALUE);
        FilesEntity expectedEntity = FileBuilder.fileDtoToEntity(fileForRequest);
        when(filesService.saveFile(fileForRequest.getFilename(), fileContent))
                .thenReturn(expectedEntity);
        // When
        ResultActions response = mockMvc.perform
                (multipart(Endpoints.FILE).file(fileContent)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param(FilesParamValues.FILENAME_PARAM, fileForRequest.getFilename()));
        // Then
        response.andExpect(status().isOk());
    }
}