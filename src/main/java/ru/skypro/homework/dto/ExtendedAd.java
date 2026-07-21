package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExtendedAd {

    @Schema(type = "integer", format = "int32", description = "id объявления")
    private int pk;

    @Schema(type = "string", description = "имя автора объявления")
    private String authorFirstName;

    @Schema(type = "string", description = "фамилия автора объявления")
    private String authorLastName;

    @Schema(type = "string", description = "описание объявления")
    private String description;

    @Schema(type = "string", description = "логин автора объявления")
    private String email;

    @Schema(type = "string", description = "ссылка на картинку объявления")
    private String image;

    @Schema(type = "string", description = "телефон автора объявления")
    private String phone;

    @Schema(type = "integer", format = "int32", description = "цена объявления")
    private int price;

    @Schema(type = "string", description = "заголовок объявления")
    private String title;
}
