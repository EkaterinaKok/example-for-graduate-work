package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "adId", source = "ad.pk")
    @Mapping(target = "createdAt", source = "createdAt") // <--- ИСПРАВЛЕНИЕ: Добавляем маппинг времени
    Comment toDto(CommentEntity entity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    CommentEntity toEntity(CreateOrUpdateComment dto);

    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    void updateFromDto(CreateOrUpdateComment dto, @MappingTarget CommentEntity entity);
}