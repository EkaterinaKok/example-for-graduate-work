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
import ru.skypro.homework.security.AuthUtils;
import ru.skypro.homework.service.CommentsService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentsController {

    private final CommentsService commentsService;
    private final AuthUtils authUtils;

    @GetMapping
    @Operation(summary = "Получение комментариев объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Comments> getComments(@PathVariable Integer adId) {
        log.info("Получение комментариев для объявления ID: {}", adId);
        Comments comments = commentsService.getCommentsByAd(adId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @Operation(summary = "Добавление комментария к объявлению")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<Comment> addComment(
            @PathVariable Integer adId,
            @RequestBody CreateOrUpdateComment dto) {

        Integer authorId = authUtils.getCurrentUserId();

        Comment comment = commentsService.addComment(adId, dto.getText(), authorId);
        return ResponseEntity.status(201).body(comment);
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

        Integer currentUserId = authUtils.getCurrentUserId();
        commentsService.deleteComment(commentId, currentUserId);
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

        Integer currentUserId = authUtils.getCurrentUserId();
        Comment comment = commentsService.updateComment(commentId, dto.getText(), currentUserId);
        return ResponseEntity.ok(comment);
    }

}
