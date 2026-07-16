package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AccessDeniedExceptionCustom;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentsRepository;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.CommentsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final AdsRepository adsRepository;
    private final UsersRepository usersRepository;
    private final CommentMapper commentMapper;

    @Override
    public Comments getCommentsByAd(Integer adPk) {
        adsRepository.findById(adPk)
                .orElseThrow(() -> new NotFoundException("Объявление с ID " + adPk + " не найдено"));

        List<CommentEntity> comments = commentsRepository.findAllByAdPk(adPk);

        List<Comment> dtoList = comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        Comments result = new Comments();
        result.setCount(comments.size());
        result.setResults(dtoList);
        return result;
    }

    @Override
    public Comment addComment(Integer adId, String text, Integer authorId) {
        AdEntity ad = adsRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Объявление с ID " + adId + " не найдено"));

        UserEntity author = usersRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + authorId + " не найден"));

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText(text);
        commentEntity.setAd(ad);
        commentEntity.setAuthor(author);

        commentEntity.setCreatedAt(System.currentTimeMillis());

        CommentEntity saved = commentsRepository.save(commentEntity);
        return commentMapper.toDto(saved);
    }

    @Override
    public void deleteComment(Integer commentId, Integer currentUserId) {
        CommentEntity comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с ID " + commentId + " не найден"));

        boolean isAuthor = comment.getAuthor().getId().equals(currentUserId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;

        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().contains("ADMIN"));
        }

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedExceptionCustom("Доступ запрещен: вы не автор и не администратор");
        }

        commentsRepository.delete(comment);
    }

    @Override
    public Comment updateComment(Integer commentId, String newText, Integer currentUserId) {
        CommentEntity comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с ID " + commentId + " не найден"));

        boolean isAuthor = comment.getAuthor().getId().equals(currentUserId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;

        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().contains("ADMIN"));
        }

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedExceptionCustom("Доступ запрещен: вы не автор и не администратор");
        }

        comment.setText(newText);
        return commentMapper.toDto(commentsRepository.save(comment));
    }

}
