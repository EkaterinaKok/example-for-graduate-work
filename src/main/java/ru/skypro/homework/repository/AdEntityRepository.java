package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;

/**
 * Репозиторий для работы с сущностью объявления ({@link AdEntity}).
 * Предоставляет стандартные CRUD-операции (создание, чтение, обновление, удаление)
 * и дополнительные методы, предоставляемые интерфейсом {@link JpaRepository}.
 *
 * <p>
 * Тип ключа — {@code Integer}, соответствующий полю {@code pk} в {@link AdEntity}.
 */
@Repository
public interface AdEntityRepository extends JpaRepository<AdEntity, Integer> {
}
