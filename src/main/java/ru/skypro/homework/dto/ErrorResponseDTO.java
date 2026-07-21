package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "DTO для представления ошибки в ответе API")
public class ErrorResponseDTO {

    @Schema(description = "Время возникновения ошибки в формате ISO 8601", example = "2025-05-08T12:34:56Z")
    private Instant timestamp;

    @Schema(description = "HTTP-статус ошибки", example = "400")
    private int status;

    @Schema(description = "Краткое описание типа ошибки", example = "Bad Request")
    private String error;

    @Schema(description = "Подробное сообщение об ошибке", example = "Идентификатор артефакта не может быть 0 или меньше")
    private String message;

    @Schema(description = "Путь запроса, вызвавшего ошибку", example = "/users/me")
    private String path;

    public ErrorResponseDTO(int status, String error, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}