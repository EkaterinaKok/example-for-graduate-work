package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO-класс для обновления пароля пользователя.
 * Содержит текущий и новый пароль.
 * Используется при обработке запроса на смену пароля авторизованным пользователем.
 */
@Data
public class NewPassword {

    /**
     * Текущий пароль пользователя.
     * Требуется для подтверждения личности перед сменой пароля.
     * Длина от 8 до 16 символов, не может быть пустым.
     */
    @Schema(type = "string", description = "текущий пароль", minLength = 8, maxLength = 16, example = "currentPassword")
    @NotBlank(message = "Текущий пароль не может быть пустым или не указанным")
    @Size(min = 8, max = 16, message = "Текущий пароль не может быть меньше 8 или больше 16")
    private String currentPassword;

    /**
     * Новый пароль пользователя.
     * Должен соответствовать требованиям безопасности.
     * Длина от 8 до 16 символов, не может быть пустым.
     */
    @Schema(type = "string", description = "новый пароль", minLength = 8, maxLength = 16, example = "newPassword")
    @NotBlank(message = "Новый пароль не может быть пустым или не указанным")
    @Size(min = 8, max = 16, message = "Новый пароль не может быть меньше 8 или больше 16")
    private String newPassword;
}
