package ru.netology.cloudservice.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservice.db.entities.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long>  {
}