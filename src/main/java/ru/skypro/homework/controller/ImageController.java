package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.ImageStorageService;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/images")
@Tag(name = "Картинки")
@RequiredArgsConstructor
public class ImageController {

    private final ImageStorageService imageStorageService; // Сервис внедряется автоматически

    @GetMapping("/{filename}")
    @Operation(summary = "Получение картинки по имени файла")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Картинка не найдена")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws MalformedURLException {

        // ✅ ВСЁ, ЧТО НУЖНО КОНТРОЛЛЕРУ: попросить ресурс у сервиса
        Resource resource = imageStorageService.getResource(filename);

        if (resource != null && resource.exists() && resource.isReadable()) {

            // 🖼️ ОПРЕДЕЛЕНИЕ ТИПА КОНТЕНТА
            String lowerFilename = filename.toLowerCase();
            String contentType;

            if (lowerFilename.endsWith(".png")) {
                contentType = MediaType.IMAGE_PNG_VALUE;
            } else if (lowerFilename.endsWith(".gif")) {
                contentType = MediaType.IMAGE_GIF_VALUE;
            } else {
                // По умолчанию считаем JPEG
                contentType = MediaType.IMAGE_JPEG_VALUE;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);

            return ResponseEntity.ok().headers(headers).body(resource);
        }

        // Если ресурса нет (файл не найден или проверка безопасности не прошла)
        return ResponseEntity.notFound().build();
    }
}
