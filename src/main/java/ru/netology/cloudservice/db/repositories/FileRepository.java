package ru.netology.cloudservice.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservice.db.entities.FileEntity;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long>  {
    @Query(nativeQuery = true, value = "SELECT * FROM file f LIMIT :limit")
    List<FileEntity> findAllByLimit(@Param("limit") Integer limit);
}