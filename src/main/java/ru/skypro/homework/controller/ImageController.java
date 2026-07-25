package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

/**
 * Контроллер для работы с изображениями.
 * Предоставляет endpoint для скачивания изображений по пути.
 */
@Tag(name = "Изображения")
@RequiredArgsConstructor
@RequestMapping
@RestController
@Slf4j
public class ImageController {

    /**
     * Сервис для работы с файлами изображений.
     */
    private final ImageService imageService;

    /**
     * Получает изображение по пути.
     *
     * @param imagePath относительный путь к изображению (без префикса /images/)
     * @return ResponseEntity с байтами изображения и contentType image/*
     */
    @Operation(summary = "Получение изображения", operationId = "getImage")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "image/*", schema = @Schema(type = "string", format = "byte")))
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping(value = "/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        log.info("=== GET IMAGE DEBUG ===");
        log.info("Image name: {}", imageName);
        
        // Формируем полный путь к изображению
        String fullPath = "/images/" + imageName;
        log.info("Full path: {}", fullPath);
        
        byte[] imageBytes = imageService.getImage(fullPath);
        
        if (imageBytes == null || imageBytes.length == 0) {
            log.warn("Image not found: {}", imageName);
            return ResponseEntity.notFound().build();
        }
        
        log.info("Image loaded successfully, size: {} bytes", imageBytes.length);
        
        // Определяем contentType по расширению файла
        String contentType = MediaType.IMAGE_JPEG_VALUE;
        if (imageName.toLowerCase().endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG_VALUE;
        } else if (imageName.toLowerCase().endsWith(".gif")) {
            contentType = MediaType.IMAGE_GIF_VALUE;
        } else if (imageName.toLowerCase().endsWith(".webp")) {
            contentType = "image/webp";
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }
}
