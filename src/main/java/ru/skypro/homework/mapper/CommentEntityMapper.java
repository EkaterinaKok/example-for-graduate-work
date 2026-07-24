package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

/**
 * MapStruct-интерфейс для маппинга сущностей комментариев (CommentEntity) и DTO.
 * Обеспечивает преобразование между {@link CommentEntity} и {@link Comment},
 * а также создание новой сущности комментария на основе входных данных.
 *
 * Конфигурация:
 * - {@code componentModel = "spring"}: генерируемый маппер регистрируется как Spring-бин.
 */
@Mapper(componentModel = "spring")
public interface CommentEntityMapper {

    /**
     * Преобразует сущность {@link CommentEntity} в DTO {@link Comment}.
     * Извлекает данные об авторе (id, имя, аватар) из связанной сущности {@link UserEntity}.
     *
     * @param entity исходная сущность комментария
     * @return DTO-представление комментария с данными об авторе
     */
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    Comment toDto(CommentEntity entity);

    /**
     * Создает новую сущность {@link CommentEntity} на основе переданных автора, объявления и текста комментария.
     * Поле {@code pk} (первичный ключ) игнорируется при создании, так как будет сгенерировано БД.
     * Связи с сущностями {@link AdEntity} и {@link UserEntity} устанавливаются явно.
     *
     * @param author      сущность автора комментария
     * @param adEntity    сущность объявления, к которому относится комментарий
     * @param createComment данные комментария (текст)
     * @return новая сущность комментария, готовая к сохранению в БД
     */
    @Mapping(target = "pk", ignore = true)
    @Mapping(source = "adEntity", target = "adEntity")
    @Mapping(source = "author", target = "author")
    CommentEntity createCommentEntity(UserEntity author, AdEntity adEntity, CreateOrUpdateComment createComment);
}