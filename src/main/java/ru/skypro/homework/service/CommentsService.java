package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentsService {

    /**
     * Получить все комментарии к конкретному объявлению
     */
    Comments getCommentsByAd(Integer adId);

    /**
     * Добавить комментарий
     */
    Comment addComment(Integer adId, String text, Integer authorId);

    /**
     * Удалить комментарий
     * @param commentId ID комментария
     */
    void deleteComment(Integer commentId);

    /**
     * Обновить текст комментария
     * @param commentId ID комментария
     * @param newText Новый текст
     * @param currentUserId ID пользователя, который пытается обновить (для проверки прав)
     */
    Comment updateComment(Integer commentId, String newText, Integer currentUserId);

}
