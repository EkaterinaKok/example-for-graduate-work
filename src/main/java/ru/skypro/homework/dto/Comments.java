package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * DTO-класс для представления коллекции комментариев с метаданными.
 * Содержит общее количество комментариев и список самих комментариев.
 * Обычно используется в ответах API при получении списка комментариев к объявлению.
 */
@Data
public class Comments {

    /**
     * Общее количество комментариев в выборке.
     */
    @Schema(type = "integer", format = "int32", description = "общее количество комментариев", example = "6")
    private int count;

    /**
     * Список комментариев, входящих в выборку.
     */
    @Schema(type = "array")
    private List<Comment> results;
}
