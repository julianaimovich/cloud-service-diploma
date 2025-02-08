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
import org.springframework.test.web.servlet.ResultActions;
import ru.netology.cloudservice.config.Constants.Endpoints;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.service.AuthService;
import ru.netology.cloudservice.util.builder.UserBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void shouldAuthenticateUserAndReturnTokenTest() throws Exception {
        // Given
        UserDto requestDto = UserBuilder.getRandomUserForRequest();
        UserDto responseDto = UserBuilder.getRandomAuthTokenForResponse();
        when(authService.authenticate(any(UserDto.class), any(HttpServletRequest.class)))
                .thenReturn(responseDto);
        // When
        ResultActions response = mockMvc.perform(post(Endpoints.LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.auth-token").value(responseDto.getAuthToken()));
    }
}