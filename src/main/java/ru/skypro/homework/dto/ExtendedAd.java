package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO-класс для представления расширенных данных об объявлении.
 * В отличие от базового {@link Ad}, содержит полную информацию об авторе
 * (имя, фамилия, email, телефон) и все атрибуты объявления.
 * Используется в API-ответах, где требуется детальное представление объявления.
 */
@Data
public class ExtendedAd {

    /**
     * Идентификатор (первичный ключ) объявления в системе.
     */
    @Schema(type = "integer", format = "int32", description = "id объявления", example = "3")
    private int pk;

    /**
     * Имя автора объявления.
     */
    @Schema(type = "string", description = "имя автора объявления", example = "2")
    private String authorFirstName;

    /**
     * Фамилия автора объявления.
     */
    @Schema(type = "string", description = "фамилия автора объявления", example = "Сидорова")
    private String authorLastName;

    /**
     * Описание объявления.
     */
    @Schema(type = "string", description = "описание объявления", example = "Велосипед двухколесный")
    private String description;

    /**
     * Email (логин) автора объявления.
     */
    @Schema(type = "string", description = "логин автора объявления", example = "12345@mail.ru")
    private String email;

    /**
     * Ссылка на изображение, связанное с объявлением.
     */
    @Schema(type = "string", description = "ссылка на картинку объявления", example = "images/58a8e2b6-2e96-4b96.jpeg")
    private String image;

    /**
     * Телефон автора объявления.
     */
    @Schema(type = "string", description = "телефон автора объявления", example = "+79028355679")
    private String phone;

    /**
     * Цена, указанная в объявлении.
     */
    @Schema(type = "integer", format = "int32", description = "цена объявления", example = "15000")
    private int price;

    /**
     * Заголовок (название) объявления.
     */
    @Schema(type = "string", description = "заголовок объявления", example = "Продам велосипед")
    private String title;
}
