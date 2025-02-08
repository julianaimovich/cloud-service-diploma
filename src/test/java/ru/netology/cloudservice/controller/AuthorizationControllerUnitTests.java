package ru.netology.cloudservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.netology.cloudservice.config.Constants;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.netology.cloudservice.util.TestConstants.UserParamValues.TEST_LOGIN;
import static ru.netology.cloudservice.util.TestConstants.UserParamValues.TEST_PASSWORD;
import static ru.netology.cloudservice.util.TestConstants.UserParamValues.TEST_TOKEN;

@WebMvcTest(AuthorizationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthorizationControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    @DisplayName("Should authenticate user and return token")
    void shouldAuthenticateUserAndReturnToken() throws Exception {
        // Given
        UserDto requestDto = new UserDto(TEST_LOGIN, TEST_PASSWORD);
        UserDto responseDto = new UserDto(TEST_TOKEN);
        // When
        when(authService.authenticate(any(UserDto.class), any(HttpServletRequest.class)))
                .thenReturn(responseDto);
        // Then
        mockMvc.perform(post(Constants.Endpoints.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.auth-token").value(TEST_TOKEN));
    }
}