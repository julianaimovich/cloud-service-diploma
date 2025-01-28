package ru.netology.cloudservice.api.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class FileSchema extends BaseSchema {
    private String filename;
    private String hash;
    private Integer size;
    private String file;

    public FileSchema(String filename, Integer size) {
        this.filename = filename;
        this.size = size;
    }
}