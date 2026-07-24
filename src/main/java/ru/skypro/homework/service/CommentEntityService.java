package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

/**
 * Интерфейс сервиса для управления комментариями к объявлениям.
 * Определяет контракт для получения, добавления, удаления и обновления комментариев,
 * а также проверки прав автора комментария.
 */
public interface CommentEntityService {

    /**
     * Получает список всех комментариев для указанного объявления.
     *
     * @param id идентификатор объявления
     * @return объект {@link Comments}, содержащий список комментариев и их количество
     */
    Comments getComments(int id);

    /**
     * Добавляет новый комментарий к указанному объявлению.
     *
     * @param id             идентификатор объявления, к которому добавляется комментарий
     * @param createComment  данные для создания комментария из DTO {@link CreateOrUpdateComment}
     * @param authentication объект аутентификации для идентификации автора комментария
     * @return DTO созданного комментария {@link Comment}
     */
    Comment addComment(int id, CreateOrUpdateComment createComment, Authentication authentication);

    /**
     * Удаляет комментарий, принадлежащий указанному объявлению.
     *
     * @param adId       идентификатор объявления
     * @param commentId  идентификатор удаляемого комментария
     */
    void deleteComment(int adId, int commentId);

    /**
     * Обновляет текст существующего комментария.
     *
     * @param adId         идентификатор объявления
     * @param commentId    идентификатор обновляемого комментария
     * @param updateComment новые данные для обновления комментария из DTO {@link CreateOrUpdateComment}
     * @return обновленное DTO комментария {@link Comment}
     */
    Comment updateComment(int adId, int commentId, CreateOrUpdateComment updateComment);

    /**
     * Проверяет, является ли указанный пользователь автором конкретного комментария.
     *
     * @param username   имя пользователя (логин) для проверки
     * @param adId       идентификатор объявления
     * @param commentId  идентификатор комментария
     * @return {@code true}, если пользователь является автором комментария, иначе {@code false}
     */
    boolean isOwner(String username, int adId, int commentId);
}