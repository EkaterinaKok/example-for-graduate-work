package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO-класс для представления информации о пользователе.
 * Содержит основные данные пользователя: идентификатор, контактные данные, роль и ссылку на аватар.
 * Используется для передачи данных о пользователе между слоями приложения
 * и для сериализации в JSON при REST-ответах.
 */
@Data
public class User {

    /**
     * Уникальный идентификатор пользователя в системе.
     */
    @Schema(type = "integer", format = "int32", description = "id пользователя", example = "1")
    private int id;

    /**
     * Логин пользователя (email).
     */
    @Schema(type = "string", description = "логин пользователя", example = "12345@mail.ru")
    private String email;

    /**
     * Имя пользователя.
     */
    @Schema(type = "string", description = "имя пользователя", example = "Екатерина")
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @Schema(type = "string", description = "фамилия пользователя", example = "Смирнова")
    private String lastName;

    /**
     * Телефон пользователя.
     */
    @Schema(type = "string", description = "телефон пользователя", example = "+79028396679")
    private String phone;

    /**
     * Роль пользователя в системе ({@link Role}).
     */
    @Schema(type = "string", description = "роль пользователя", example = "USER")
    private Role role;

    /**
     * Ссылка на изображение (аватар) пользователя.
     */
    @Schema(type = "string", description = "ссылка на аватар пользователя", example = "images/58a8e2b6-2e96-4b96.jpeg")
    private String image;
}
