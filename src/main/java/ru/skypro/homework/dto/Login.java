package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO-класс для передачи данных при аутентификации пользователя.
 * Содержит логин (в формате email) и пароль для входа в систему.
 * Используется в контроллере авторизации при обработке запроса на вход.
 */
@Data
public class Login {

    /**
     * Логин пользователя (должен быть в формате email).
     * Длина от 4 до 32 символов.
     */
    @Schema(type = "string", description = "логин", minLength = 4, maxLength = 32, example = "12345@mail.ru")
    @NotEmpty(message = "Логин не может быть пустым или не указанным")
    @Email(message = "Логин должен быть формата электронной почты: example@mail.ru")
    @Size(min = 4, max = 32, message = "Логин не может быть меньше 4 и больше 32")
    private String username;

    /**
     * Пароль пользователя для аутентификации.
     * Длина от 8 до 16 символов.
     */
    @Schema(type = "string", description = "пароль", minLength = 8, maxLength = 16, example = "password")
    @NotEmpty(message = "Пароль не может быть пустым или не указанным")
    @Size(min = 8, max = 16, message = "Пароль не может быть меньше 8 и больше 16")
    private String password;
}

