package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentEntityService {

    Comments getComments(int id);

    Comment addComment(int id, CreateOrUpdateComment createComment, Authentication authentication);

    void deleteComment(int adId, int commentId);

    Comment updateComment(int adId, int commentId, CreateOrUpdateComment updateComment);

    boolean isOwner(String username, int adId, int commentId);
}