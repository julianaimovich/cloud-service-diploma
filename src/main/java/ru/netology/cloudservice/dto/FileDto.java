package ru.netology.cloudservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.File;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDto {

    private String filename;
    private Integer size;
    @JsonIgnore
    private File file;
    @JsonIgnore
    private String contentType;

    public FileDto(String filename) {
        this.filename = filename;
    }

    public FileDto(String filename, Integer size) {
        this.filename = filename;
        this.size = size;
    }
}