package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO-класс для обновления персональных данных пользователя.
 * Содержит поля с валидацией для изменения имени, фамилии и телефона.
 * Используется в контроллере при обработке запроса на частичное обновление профиля пользователя.
 */
@Data
public class UpdateUser {

    /**
     * Имя пользователя.
     * Длина от 3 до 10 символов, не может быть пустым.
     */
    @Schema(type = "string", description = "имя пользователя", minLength = 3, maxLength = 10, example = "Екатерина")
    @NotBlank(message = "Имя пользователя не может быть пустым или не указанным")
    @Size(min = 3, max = 10, message = "Имя пользователя не может быть меньше 3 символов и не больше 10 символов")
    private String firstName;

    /**
     * Фамилия пользователя.
     * Длина от 3 до 10 символов, не может быть пустым.
     */
    @Schema(type = "string", description = "фамилия пользователя", minLength = 3, maxLength = 10, example = "Смирнова")
    @NotBlank(message = "Фамилия пользователя не может быть пустым или не указанным")
    @Size(min = 3, max = 10, message = "Фамилия пользователя не может быть меньше 3 символов и не больше 10 символов")
    private String lastName;

    /**
     * Номер телефона пользователя.
     * Должен соответствовать шаблону российского номера: +7...
     */
    @Schema(type = "string", description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", example = "+79082491276")
    @NotBlank(message = "Телефон пользователя не может быть пустым или не указанным")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Номер телефона должен быть указан в формате: +7(987)654-32-10")
    private String phone;
}
