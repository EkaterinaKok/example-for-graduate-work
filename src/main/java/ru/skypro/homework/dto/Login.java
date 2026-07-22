package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Login {

    @Schema(type = "string", description = "логин", minLength = 4, maxLength = 32)
    @NotEmpty(message = "Логин не может быть пустым или не указанным")
    @Email(message = "Логин должен быть формата электронной почты: example@mail.ru")
    @Size(min = 4, max = 32, message = "Логин не может быть меньше 4 и больше 32")
    private String username;

    @Schema(type = "string", description = "пароль", minLength = 8, maxLength = 16)
    @NotEmpty(message = "Пароль не может быть пустым или не указанным")
    @Size(min = 8, max = 16, message = "Пароль не может быть меньше 8 и больше 16")
    private String password;
}
