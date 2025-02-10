package ru.netology.cloudservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.cloudservice.model.FilesEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilesRepository extends JpaRepository<FilesEntity, Long>  {
    @Transactional
    @Query(nativeQuery = true, value = "SELECT * FROM files f LIMIT :limit")
    List<FilesEntity> findAllByLimit(@Param("limit") Integer limit);

    @Transactional
    Optional<FilesEntity> findByFilename(String filename);

    @Transactional
    void deleteByFilename(String filename);
}