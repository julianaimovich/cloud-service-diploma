package ru.netology.cloudservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseConverter {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> String convertClassToJsonString(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}