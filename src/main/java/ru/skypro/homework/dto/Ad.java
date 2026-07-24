package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO-класс для представления краткой информации об объявлении.
 * Используется для передачи данных об объявлении между слоями приложения
 * и для сериализации в JSON при REST-ответах.
 */
@Data
public class Ad {

    /**
     * Идентификатор автора объявления.
     */
    @Schema(type = "integer", format = "int32", description = "id автора объявления", example = "5")
    private int author;

    /**
     * Ссылка на изображение, связанное с объявлением.
     */
    @Schema(type = "string", description = "ссылка на картинку объявления", example = "images/58a8e2b6-2e96-4b96.jpeg")
    private String image;

    /**
     * Первичный ключ (идентификатор) объявления в системе.
     */
    @Schema(type = "integer", format = "int32", description = "id объявления", example = "5")
    private int pk;

    /**
     * Цена, указанная в объявлении.
     */
    @Schema(type = "integer", format = "int32", description = "цена объявления", example = "1500" )
    private int price;

    /**
     * Заголовок (название) объявления.
     */
    @Schema(type = "string", description = "заголовок объявления", example = "Продам велосипед")
    private String title;
}
