package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью комментария ({@link CommentEntity}).
 * Расширяет {@link JpaRepository} для получения стандартного CRUD-функционала.
 * Содержит дополнительный метод для поиска комментария по комбинации ID объявления и ID комментария,
 * что полезно при проверке принадлежности комментария конкретному объявлению.
 *
 * <p>
 * Тип ключа — {@code Integer}, соответствующий полю {@code pk} в {@link CommentEntity}.
 */
@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {

    /**
     * Находит комментарий по идентификатору объявления и идентификатору самого комментария.
     * Позволяет убедиться, что запрашиваемый комментарий действительно относится к указанному объявлению.
     *
     * @param adId       идентификатор объявления
     * @param commentId  идентификатор комментария
     * @return {@link Optional} с найденным комментарием или пустым Optional, если комментарий не найден
     */
    Optional<CommentEntity> findByAdEntity_PkAndPk(int adId, int commentId);
}