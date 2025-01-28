package ru.netology.cloudservice.schemas;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ErrorSchema extends ResponseSchema {
    private String message;
    private int id;
}