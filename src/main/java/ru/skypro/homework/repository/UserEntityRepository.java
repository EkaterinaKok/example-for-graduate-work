package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью пользователя ({@link UserEntity}).
 * Расширяет {@link JpaRepository} для получения стандартного CRUD-функционала.
 * Содержит дополнительный метод для поиска пользователя по логину (username),
 * который используется при аутентификации и проверке уникальности логина при регистрации.
 *
 * <p>
 * Тип ключа — {@code Integer}, соответствующий полю {@code id} в {@link UserEntity}.
 */
@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Находит пользователя по его логину (username).
     * Поле username в {@link UserEntity} объявлено как уникальное, поэтому метод вернет
     * не более одного результата.
     *
     * @param username логин пользователя (в формате email)
     * @return {@link Optional} с найденным пользователем или пустым Optional, если пользователь не найден
     */
    Optional<UserEntity> findByUsername(String username);
}