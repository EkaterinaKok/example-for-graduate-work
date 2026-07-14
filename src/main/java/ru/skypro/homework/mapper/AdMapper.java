package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.entity.AdEntity;

@Mapper(componentModel = "spring")
public interface AdMapper {

    // МАППИНГ ENTITY -> DTO
    @Mapping(target = "pk", source = "pk") // Явное указание (хотя часто работает и без него)
    @Mapping(target = "image", source = "image") // image в Entity называется просто image
    @Mapping(target = "price", source = "price")
    @Mapping(target = "title", source = "title")
    // ГЛАВНОЕ ИСПРАВЛЕНИЕ: берем id из объекта author.
    // Важно: source = "author.id" работает, только если в UserEntity поле @Id называется "id".
    @Mapping(target = "authorId", source = "author.id")
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
    void updateFromDto(CreateOrUpdateAd dto, @MappingTarget AdEntity entity);
}