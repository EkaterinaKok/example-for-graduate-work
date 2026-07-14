package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdEntity;

public interface AdsRepository extends JpaRepository<AdEntity, Integer> {
    // Можно добавить кастомные методы, если понадобятся позже
}
