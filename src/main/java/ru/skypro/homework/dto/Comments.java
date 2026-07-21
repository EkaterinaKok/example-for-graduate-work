package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class Comments {

    @Schema(type = "integer", format = "int32", description = "общее количество комментариев")
    private int count;

    @Schema(type = "array")
    private List<Comment> results;
}
