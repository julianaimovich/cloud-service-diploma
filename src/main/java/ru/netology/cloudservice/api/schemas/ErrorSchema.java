package ru.netology.cloudservice.api.schemas;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ErrorSchema extends BaseSchema {
    private String message;
    private int id;
}