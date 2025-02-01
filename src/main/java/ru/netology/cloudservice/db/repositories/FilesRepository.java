package ru.netology.cloudservice.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservice.db.entities.FilesEntity;

import java.util.List;

@Repository
public interface FilesRepository extends JpaRepository<FilesEntity, Long>  {
    @Query(nativeQuery = true, value = "SELECT * FROM files f LIMIT :limit")
    List<FilesEntity> findAllByLimit(@Param("limit") Integer limit);
}