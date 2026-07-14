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
    // ГЛАВНОЕ: Берем ID автора из объекта author.
    // ВАЖНО: source = "author.id" сработает, только если в UserEntity поле @Id называется "id".
    // Если в UserEntity оно называется "pk", замени на "author.pk".
    @Mapping(target = "authorId", source = "author.id")

    // Берем ID объявления из объекта ad.
    // Проверь в AdEntity: поле называется "pk" или "id"? У тебя в коде выше было "pk".
    @Mapping(target = "adId", source = "ad.pk")
    Comment toDto(CommentEntity entity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", ignore = true) // createdAt есть в Entity, игнорируем при создании
    @Mapping(target = "author", ignore = true)    // автор (объект) не создаем через DTO
    @Mapping(target = "ad", ignore = true)        // объявление (объект) не создаем через DTO
    CommentEntity toEntity(CreateOrUpdateComment dto);

    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    void updateFromDto(CreateOrUpdateComment dto, @MappingTarget CommentEntity entity);
}