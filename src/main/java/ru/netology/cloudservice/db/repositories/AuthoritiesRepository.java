package ru.netology.cloudservice.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservice.db.entities.AuthoritiesEntity;

import java.util.Optional;

@Repository
public interface AuthoritiesRepository extends JpaRepository<AuthoritiesEntity, Long> {
    Optional<AuthoritiesEntity> findByLogin(String login);
}