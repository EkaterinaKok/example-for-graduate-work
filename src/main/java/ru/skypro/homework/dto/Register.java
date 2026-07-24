package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO-класс для регистрации нового пользователя.
 * Содержит все необходимые данные для создания учетной записи:
 * логин (email), пароль, имя, фамилию, телефон и роль.
 * Используется в контроллере регистрации при обработке запроса на создание пользователя.
 */
@Data
public class Register {

    /**
     * Логин пользователя (должен быть в формате email).
     * Длина от 4 до 32 символов.
     */
    @Schema(type = "string", description = "логин", minLength = 4, maxLength = 32, example = "12345@mail.ru")
    @NotBlank(message = "Логин не может быть пустым или не указанным")
    @Email(message = "Логин должен быть формата электронной почты: example@mail.ru")
    @Size(min = 4, max = 32, message = "Логин не может быть меньше 4 символов и не больше 32 символов")
    private String username;

    /**
     * Пароль нового пользователя.
     * Длина от 8 до 16 символов, не может быть пустым.
     */
    @Schema(type = "string", description = "пароль", minLength = 8, maxLength = 16, example = "password")
    @NotBlank(message = "Пароль не может быть пустым или не указанным")
    @Size(min = 8, max = 16, message = "Пароль не может быть меньше 8 символов и не больше 16 символов")
    private String password;

    /**
     * Имя пользователя.
     * Длина от 2 до 16 символов, не может быть пустым.
     */
    @Schema(type = "string", description = "имя пользователя", minLength = 2, maxLength = 16, example = "Екатерина")
    @NotBlank(message = "Имя пользователя не может быть пустым или не указанным")
    @Size(min = 2, max = 16, message = "Имя пользователя не может быть меньше 2 символов и не больше 16 символов")
    private String firstName;

    /**
     * Фамилия пользователя.
     * Длина от 2 до 16 символов, не может быть пустым.
     */
    @Schema(type = "string", description = "фамилия пользователя", minLength = 2, maxLength = 16, example = "Смирнова")
    @NotBlank(message = "Фамилия пользователя не может быть пустым или не указанным")
    @Size(min = 2, max = 16, message = "Фамилия пользователя не может быть меньше 2 символов и не больше 16 символов")
    private String lastName;

    /**
     * Номер телефона пользователя.
     * Должен соответствовать шаблону российского номера: +7...
     */
    @Schema(type = "string", description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", example = "+79876543210")
    @NotBlank(message = "Телефон пользователя не может быть пустым или не указанным")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Номер телефона должен быть указан в формате: +79876543210")
    private String phone;

    /**
     * Роль пользователя в системе.
     */
    @Schema(type = "string", description = "роль пользователя", example = "USER")
    private Role role;
}
