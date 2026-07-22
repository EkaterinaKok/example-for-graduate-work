package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Ad {

    @Schema(type = "integer", format = "int32", description = "id автора объявления")
    private int author;

    @Schema(type = "string", description = "ссылка на картинку объявления")
    private String image;

    @Schema(type = "integer", format = "int32", description = "id объявления")
    private int pk;

    @Schema(type = "integer", format = "int32", description = "цена объявления")
    private int price;

    @Schema(type = "string", description = "заголовок объявления")
    private String title;
}
