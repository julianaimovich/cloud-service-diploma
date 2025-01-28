package ru.netology.cloudservice.db.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private Integer size;

    @Column(name = "file_content", nullable = false)
    @Lob
    private Blob fileContent;
}