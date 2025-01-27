package ru.netology.cloudservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservice.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
}