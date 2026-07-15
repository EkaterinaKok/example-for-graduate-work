package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;


public interface AdsRepository extends JpaRepository<AdEntity, Integer> {

    // Этот метод Spring Data JPA создаст автоматически по имени
    List<AdEntity> findAllByAuthorId(Integer authorId);
}
