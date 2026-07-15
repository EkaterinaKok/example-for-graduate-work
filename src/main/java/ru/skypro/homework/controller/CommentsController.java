package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentsService;

import java.util.Collections;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentsController {

    private final CommentsService commentsService;

    private static final Integer MOCK_ID = 1;

    @GetMapping
    @Operation(summary = "Получение комментариев объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Comments> getComments(@PathVariable Integer adId) {
        log.info("Получение комментариев для объявления ID: {}", adId);
        Comments comments = new Comments();
        comments.setCount(0);
        comments.setResults(Collections.emptyList());
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @Operation(summary = "Добавление комментария к объявлению")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<Comment> addComment(
            @PathVariable Integer adId,
            @RequestBody String text, // <-- Берем просто строку JSON или plain text
            @RequestAttribute("userId") Integer authorId) { // <-- Получаем ID из SecurityContext

        try {
            // ✅ ПРАВИЛЬНЫЙ ВЫЗОВ: adId, text (строка), authorId
            Comment comment = commentsService.addComment(adId, text, authorId);
            return ResponseEntity.status(201).body(comment);
        } catch (RuntimeException e) {
            // Для этапа SkyPro проще ловить RuntimeException, так как мы кидаем их в сервисе
            log.error("Ошибка при добавлении комментария: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Удаление комментария")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<?> deleteComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId) {
        log.info("Удаление комментария ID: {} из объявления ID: {}", commentId, adId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "Обновление комментария")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            @RequestBody CreateOrUpdateComment dto) {
        log.info("Обновление комментария ID: {}. Новый текст: {}", commentId, dto.getText());
        Comment comment = new Comment();
        comment.setPk(commentId);
        comment.setText(dto.getText());
        return ResponseEntity.ok(comment);
    }

}
