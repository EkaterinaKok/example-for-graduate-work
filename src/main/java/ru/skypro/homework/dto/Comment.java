package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Schema(description = "Уникальный идентификатор комментария", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer pk;

    @Schema(description = "Текст комментария", example = "Отличный товар!")
    private String text;

    @Schema(description = "ID автора комментария", example = "5")
    private Integer authorId;

    @Schema(description = "ID объявления, к которому относится комментарий", example = "20")
    private Integer adId;

    @Schema(description = "Дата создания комментария (Unix timestamp, ms)", example = "1715623456789")
    private Long createdAt;

    @Schema(description = "Ссылка на аватарку автора", example = "/images/kosmos.jpeg")
    private String image;

}

