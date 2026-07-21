package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class Ads {

    @Schema(type = "integer", format = "int32", description = "общее количество объявлений")
    private int count;

    @Schema(type = "array")
    private List<Ad> results;
}
