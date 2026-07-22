package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewPassword {

    @Schema(type = "string", description = "текущий пароль", minLength = 8, maxLength = 16)
    @NotBlank(message = "Текущий пароль не может быть пустым или не указанным")
    @Size(min = 8, max = 16, message = "Текущий пароль не может быть меньше 8 или больше 16")
    private String currentPassword;

    @Schema(type = "string", description = "новый пароль", minLength = 8, maxLength = 16)
    @NotBlank(message = "Новый пароль не может быть пустым или не указанным")
    @Size(min = 8, max = 16, message = "Новый пароль не может быть меньше 8 или больше 16")
    private String newPassword;
}
