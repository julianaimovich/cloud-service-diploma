package ru.netology.cloudservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservice.model.AuthoritiesEntity;

import java.util.Optional;

@Repository
public interface AuthoritiesRepository extends JpaRepository<AuthoritiesEntity, Long> {
    Optional<AuthoritiesEntity> findByLogin(String login);
}