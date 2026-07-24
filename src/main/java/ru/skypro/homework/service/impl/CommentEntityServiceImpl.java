package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdEntityNotFoundException;
import ru.skypro.homework.exception.CommentEntityNotFoundException;
import ru.skypro.homework.exception.UserEntityNotFoundException;
import ru.skypro.homework.mapper.CommentEntityMapper;
import ru.skypro.homework.repository.AdEntityRepository;
import ru.skypro.homework.repository.CommentEntityRepository;
import ru.skypro.homework.repository.UserEntityRepository;
import ru.skypro.homework.service.CommentEntityService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для управления комментариями к объявлениям.
 * Предоставляет функционал для получения списка комментариев, добавления, обновления и удаления комментариев,
 * а также проверки прав владельца комментария.
 */
@RequiredArgsConstructor
@Service
public class CommentEntityServiceImpl implements CommentEntityService {

    /**
     * Репозиторий для доступа к данным комментариев в базе данных.
     */
    private final CommentEntityRepository commentEntityRepository;

    /**
     * Маппер для преобразования сущностей комментариев ({@link CommentEntity}) в DTO и обратно.
     */
    private final CommentEntityMapper commentEntityMapper;

    /**
     * Репозиторий для доступа к данным объявлений в базе данных.
     * Используется для проверки существования объявления перед операциями с комментариями.
     */
    private final AdEntityRepository adEntityRepository;

    /**
     * Репозиторий для доступа к данным пользователей в базе данных.
     * Используется для определения автора комментария по данным аутентификации.
     */
    private final UserEntityRepository userEntityRepository;

    /**
     * Получает список всех комментариев для указанного объявления.
     * Формирует ответ {@link Comments}, содержащий список комментариев и их общее количество.
     *
     * @param id идентификатор объявления
     * @return объект {@link Comments} со списком комментариев к объявлению и их количеством
     * @throws AdEntityNotFoundException если объявление с указанным ID не найдено
     */
    @Transactional(readOnly = true)
    @Override
    public Comments getComments(int id) {
        AdEntity adEntity = adEntityRepository.findById(id).orElseThrow(() -> new AdEntityNotFoundException("Объявление не найдено"));
        List<Comment> results = adEntity.getCommentEntities().stream().map(commentEntityMapper::toDto).toList();

        Comments comments = new Comments();
        comments.setResults(results);
        comments.setCount(results.size());
        return comments;
    }

    /**
     * Добавляет новый комментарий к указанному объявлению.
     * Определяет автора комментария по объекту аутентификации, создает сущность комментария с помощью маппера,
     * добавляет её в коллекцию комментариев объявления и сохраняет в БД.
     *
     * @param id             идентификатор объявления, к которому добавляется комментарий
     * @param createComment  данные для создания комментария из DTO {@link CreateOrUpdateComment}
     * @param authentication объект аутентификации для идентификации автора комментария
     * @return DTO созданного комментария {@link Comment}
     * @throws AdEntityNotFoundException    если объявление не найдено
     * @throws UserEntityNotFoundException  если пользователь (автор комментария) не найден
     */
    @Transactional
    @Override
    public Comment addComment(int id, CreateOrUpdateComment createComment, Authentication authentication) {
        UserEntity userEntity = userEntityRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserEntityNotFoundException("Пользователь не найден"));
        AdEntity adEntity = adEntityRepository.findById(id).orElseThrow(() -> new AdEntityNotFoundException("Объявление не найдено"));
        List<CommentEntity> commentEntities = adEntity.getCommentEntities();

        CommentEntity commentEntity = commentEntityMapper.createCommentEntity(userEntity, adEntity, createComment);
        commentEntities.add(commentEntity);
        CommentEntity savedCommentEntity = commentEntityRepository.save(commentEntity);

        return commentEntityMapper.toDto(savedCommentEntity);
    }

    /**
     * Удаляет комментарий, принадлежащий указанному объявлению.
     * Использует кастомный метод репозитория для поиска комментария по комбинации ID объявления и ID комментария.
     *
     * @param adId       идентификатор объявления
     * @param commentId  идентификатор удаляемого комментария
     * @throws CommentEntityNotFoundException если комментарий не найден
     */
    @Transactional
    @Override
    public void deleteComment(int adId, int commentId) {
        CommentEntity commentEntity = commentEntityRepository.findByAdEntity_PkAndPk(adId, commentId).orElseThrow(() -> new CommentEntityNotFoundException("Комментарий не найден"));

        commentEntityRepository.delete(commentEntity);
    }

    /**
     * Обновляет текст существующего комментария.
     * Находит комментарий по комбинации ID объявления и ID комментария, изменяет поле текста и сохраняет изменения в БД.
     *
     * @param adId         идентификатор объявления
     * @param commentId    идентификатор обновляемого комментария
     * @param updateComment новые данные для обновления комментария из DTO {@link CreateOrUpdateComment}
     * @return обновленное DTO комментария {@link Comment}
     * @throws CommentEntityNotFoundException если комментарий не найден
     */
    @Transactional
    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment updateComment) {
        CommentEntity commentEntity = commentEntityRepository.findByAdEntity_PkAndPk(adId, commentId).orElseThrow(() -> new CommentEntityNotFoundException("Комментарий не найден"));

        commentEntity.setText(updateComment.getText());
        commentEntityRepository.save(commentEntity);

        return commentEntityMapper.toDto(commentEntity);
    }

    /**
     * Проверяет, является ли указанный пользователь автором конкретного комментария к объявлению.
     * <p>
     * Примечание: в текущей реализации при отсутствии комментария метод возвращает {@code true}.
     * Это может быть потенциальной логической ошибкой и требует дополнительного анализа требований.
     *
     * @param username   имя пользователя (логин) для проверки
     * @param adId       идентификатор объявления
     * @param commentId  идентификатор комментария
     * @return {@code true}, если пользователь является автором комментария, иначе {@code false}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean isOwner(String username, int adId, int commentId) {
        Optional<CommentEntity> byAdEntityPkAndPk = commentEntityRepository.findByAdEntity_PkAndPk(adId, commentId);

        if (byAdEntityPkAndPk.isEmpty()) {
            return true;
        }

        return byAdEntityPkAndPk
                .map(comm -> comm.getAuthor().getUsername().equals(username))
                .orElse(false);
    }
}
