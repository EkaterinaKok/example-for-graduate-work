package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.entity.AdEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AdMapper {

    @Mappings({
            @Mapping(target = "pk", source = "pk"),
            @Mapping(target = "image", source = "image"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "authorId", source = "author.id"),
    })
    Ad toDto(AdEntity entity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "description", source = "description")
    @Mapping(target = "author", ignore = true)
    AdEntity toEntity(CreateOrUpdateAd dto);

    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "description", source = "description")
    void updateFromDto(CreateOrUpdateAd dto, @MappingTarget AdEntity entity);

}