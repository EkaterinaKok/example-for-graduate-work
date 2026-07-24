package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO-класс для создания или обновления комментария.
 * Содержит поле с валидацией текста комментария.
 * Используется в REST-контроллерах при обработке запросов на добавление и редактирование комментариев.
 */
@Data
public class CreateOrUpdateComment {

    /**
     * Текст комментария.
     * Должен содержать от 8 до 64 символов, не может быть пустым.
     */
    @Schema(type = "string", description = "текст комментария", minLength = 8, maxLength = 64, example = "Можно будет оформить доставку?")
    @NotBlank(message = "Текст комментария не может быть пустым или не указанным")
    @Size(min = 8, max = 64, message = "Текст комментария не может быть меньше 8 или больше 64")
    private String text;
}