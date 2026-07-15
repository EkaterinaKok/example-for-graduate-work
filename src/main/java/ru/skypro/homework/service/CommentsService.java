package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;

public interface CommentsService {

    Comments getCommentsByAd(Integer adId);
    Comment addComment(Integer adId, String text, Integer authorId);
    void deleteComment(Integer commentId, Integer currentUserId);
    Comment updateComment(Integer commentId, String newText, Integer currentUserId);

}
