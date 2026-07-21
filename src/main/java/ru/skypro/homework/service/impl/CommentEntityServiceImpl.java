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

@RequiredArgsConstructor
@Service
public class CommentEntityServiceImpl implements CommentEntityService {

    private final CommentEntityRepository commentEntityRepository;

    private final CommentEntityMapper commentEntityMapper;

    private final AdEntityRepository adEntityRepository;

    private final UserEntityRepository userEntityRepository;

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

    @Transactional
    @Override
    public void deleteComment(int adId, int commentId) {
        CommentEntity commentEntity = commentEntityRepository.findByAdEntity_PkAndPk(adId, commentId).orElseThrow(() -> new CommentEntityNotFoundException("Комментарий не найден"));

        commentEntityRepository.delete(commentEntity);
    }

    @Transactional
    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment updateComment) {
        CommentEntity commentEntity = commentEntityRepository.findByAdEntity_PkAndPk(adId, commentId).orElseThrow(() -> new CommentEntityNotFoundException("Комментарий не найден"));

        commentEntity.setText(updateComment.getText());
        commentEntityRepository.save(commentEntity);

        return commentEntityMapper.toDto(commentEntity);
    }

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
