package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateAd {
    @Schema(description = "Заголовок объявления", example = "Продам велосипед", maxLength = 100, minLength = 3)
    private String title;

    @Schema(description = "Описание объявления", example = "Велосипед в отличном состоянии...")
    private String description; // <-- ДОБАВЬ ЭТУ СТРОКУ

    @Schema(description = "Цена объявления в рублях", example = "15000", minimum = "0")
    private Integer price;

    @Schema(description = "Ссылка на изображение", example = "https://site.com/img/1.jpg")
    private String image;
}
