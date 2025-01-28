package ru.netology.cloudservice.schemas;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ResponseSchema {

    public static void write(HttpServletResponse response, Object json, HttpStatus code) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(code.value());
        try (var writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(json));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}