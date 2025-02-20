package ru.netology.cloudservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {

    private static final AtomicLong counter = new AtomicLong(1);

    private String message;
    private Long id;

    public ErrorDto(String message) {
        this.message = message;
        this.id = generateId();
    }

    public static long generateId() {
        return counter.incrementAndGet();
    }
}