package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO-класс для создания или обновления объявления.
 * Содержит поля с валидацией, необходимой для корректного заполнения данных объявления.
 * Используется в REST-контроллерах при обработке запросов на создание и обновление объявлений.
 */
@Data
public class CreateOrUpdateAd {

    /**
     * Заголовок объявления.
     * Должен содержать от 4 до 32 символов, не может быть пустым.
     */
    @Schema(type = "string", description = "заголовок объявления", minLength = 4, maxLength = 32, example = "Продам велосипед.")
    @NotBlank(message = "Заголовок объявления не может быть пустым или не указанным")
    @Size(min = 4, max = 32, message = "Размер заголовка не может быть меньше 4-ёх символов и не больше 32-ух символов")
    private String title;

    /**
     * Описание объявления.
     * Должно содержать от 8 до 64 символов, не может быть пустым.
     */
    @Schema(type = "string", description = "описание объявления", minLength = 8, maxLength = 64, example = "Велосипед двухколесный в хорошем состоянии")
    @NotBlank(message = "Описание объявления не может быть пустым или не указанным")
    @Size(min = 8, max = 64, message = "Размер описания не должен быть меньше 8 символом и не превышать 64 символа")
    private String description;

    /**
     * Цена объявления.
     * Должна быть указана, не может быть отрицательной, максимальное значение — 10 000 000.
     */
    @Schema(type = "integer", format = "int32", description = "цена объявления", minimum = "0", maximum = "10000000", example = "15000")
    @NotNull(message = "Цена объявления должна быть указана")
    @PositiveOrZero(message = "Цена объявления должна быть 0 или больше")
    @Max(value = 10_000_000, message = "Цена объявления не может быть больше 10_000_000")
    private Integer price;
}
