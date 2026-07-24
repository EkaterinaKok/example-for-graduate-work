package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO-класс для представления отдельного комментария.
 * Содержит данные об авторе, тексте, дате создания и идентификаторах комментария.
 * Используется для передачи информации о комментарии между слоями приложения
 * и сериализации в JSON при REST-ответах.
 */
@Data
public class Comment {

    /**
     * Идентификатор автора комментария.
     */
    @Schema(type = "integer", format = "int32", description = "id автора комментария", example = "2")
    private int author;

    /**
     * Имя автора комментария.
     */
    @Schema(type = "string", description = "имя создателя комментария", example = "Екатерина")
    private String authorFirstName;

    /**
     * Ссылка на изображение (аватар) автора комментария.
     */
    @Schema(type = "string", description = "ссылка на аватар автора комментария", example = "images/58a8e2b6-2e96-4b96.jpeg")
    private String authorImage;

    /**
     * Дата и время создания комментария, представленные в виде количества миллисекунд,
     * прошедших с 00:00:00 01.01.1970 (Unix epoch).
     */
    @Schema(type = "integer", format = "int64", description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    private long createdAt;

    /**
     * Первичный ключ (идентификатор) комментария в системе.
     */
    @Schema(type = "integer", format = "int32", description = "id комментария", example = "3")
    private int pk;

    /**
     * Текст комментария.
     */
    @Schema(type = "string", description = "текст комментария", example = "Где можно посмотреть?")
    private String text;
}