package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
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
        // 1. Проверяем существование объявления. Если нет - ошибка 404
        adsRepository.findById(adPk)
                .orElseThrow(() -> new RuntimeException("Объявление с ID " + adPk + " не найдено"));

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
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        UserEntity author = usersRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Пользователь-автор не найден"));

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText(text);
        commentEntity.setAd(ad);
        commentEntity.setAuthor(author);

        // Время создания в миллисекундах (как в твоем ТЗ)
        commentEntity.setCreatedAt(System.currentTimeMillis());

        CommentEntity saved = commentsRepository.save(commentEntity);
        return commentMapper.toDto(saved);
    }

    @Override
    public void deleteComment(Integer commentId, Integer currentUserId) {
        CommentEntity comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        // 🔥 КРИТИЧЕСКАЯ ПРОВЕРКА: Удалять может только автор комментария
        if (!comment.getAuthor().getId().equals(currentUserId)) {
            throw new RuntimeException("Доступ запрещен: нельзя удалять чужие комментарии");
        }

        commentsRepository.delete(comment);
    }

    @Override
    public Comment updateComment(Integer commentId, String newText, Integer currentUserId) {
        CommentEntity comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        // 🔥 ПРОВЕРКА ПРАВ: Редактировать может только автор
        if (!comment.getAuthor().getId().equals(currentUserId)) {
            throw new RuntimeException("Доступ запрещен: нельзя редактировать чужие комментарии");
        }

        comment.setText(newText);
        // Если в CommentEntity есть поле updatedAt, раскомментируй:
        // comment.setUpdatedAt(System.currentTimeMillis());

        return commentMapper.toDto(commentsRepository.save(comment));
    }
}
