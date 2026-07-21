package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUser {

    @Schema(type = "string", description = "имя пользователя", minLength = 3, maxLength = 10)
    @NotBlank(message = "Имя пользователя не может быть пустым или не указанным")
    @Size(min = 3, max = 10, message = "Имя пользователя не может быть меньше 3 символов и не больше 10 символов")
    private String firstName;

    @Schema(type = "string", description = "фамилия пользователя", minLength = 3, maxLength = 10)
    @NotBlank(message = "Фамилия пользователя не может быть пустым или не указанным")
    @Size(min = 3, max = 10, message = "Фамилия пользователя не может быть меньше 3 символов и не больше 10 символов")
    private String lastName;

    @Schema(type = "string", description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @NotBlank(message = "Телефон пользователя не может быть пустым или не указанным")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Номер телефона должен быть указан в формате: +7(987)654-32-10")
    private String phone;
}
