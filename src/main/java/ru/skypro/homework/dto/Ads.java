package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * DTO-класс для представления коллекции объявлений с метаданными.
 * Содержит общее количество объявлений и список самих объявлений.
 * Обычно используется в ответах API при получении списка объявлений.
 */
@Data
public class Ads {

    /**
     * Общее количество объявлений в выборке.
     */
    @Schema(type = "integer", format = "int32", description = "общее количество объявлений", example = "10")
    private int count;

    /**
     * Список объявлений, входящих в выборку.
     */
    @Schema(type = "array")
    private List<Ad> results;
}
