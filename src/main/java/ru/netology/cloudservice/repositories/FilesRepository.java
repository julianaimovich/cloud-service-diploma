package ru.netology.cloudservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservice.entities.Files;

@Repository
public interface FilesRepository extends JpaRepository<Files, Long>  {
}