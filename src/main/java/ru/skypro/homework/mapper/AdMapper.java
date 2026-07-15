package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.entity.AdEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdMapper {

    // МАППИНГ ENTITY -> DTO
    @Mappings({
            @Mapping(target = "pk", source = "pk"),
            @Mapping(target = "image", source = "image"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "title", source = "title"),
            // Берем ID из вложенного объекта author.user.id
            @Mapping(target = "authorId", source = "author.id"),
            // САМОЕ ВАЖНОЕ: Игнорируем попытку маппить весь объект 'author' целиком.
            // Это уберет предупреждение "Unmapped target property: author" из логов.
    })
    Ad toDto(AdEntity entity);

    // МАППИНГ DTO -> ENTITY (для создания)
    // description есть в Entity, но нет в CreateOrUpdateAd? Если в CreateOrUpdateAd нет description,
    // то при создании оно будет null. Если нужно, добавь поле description в CreateOrUpdateAd.
    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", ignore = true) // createdAt есть в Entity, его игнорируем при создании
    @Mapping(target = "comments", ignore = true) // comments - это связь, её не маппим вручную
    AdEntity toEntity(CreateOrUpdateAd dto);

    // ОБНОВЛЕНИЕ
    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateFromDto(CreateOrUpdateAd dto, @MappingTarget AdEntity entity);
}