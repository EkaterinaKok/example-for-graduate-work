package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

/**
 * MapStruct-интерфейс для маппинга сущностей объявлений (AdEntity) и DTO.
 * Обеспечивает преобразование между {@link AdEntity} и различными DTO-представлениями:
 * базовым ({@link Ad}), расширенным ({@link ExtendedAd}), а также создание и обновление сущностей
 * на основе входных данных ({@link CreateOrUpdateAd}).
 *
 * Конфигурация:
 * - {@code componentModel = "spring"}: генерируемый маппер регистрируется как Spring-бин.
 * - {@code nullValuePropertyMappingStrategy = IGNORE}: при маппинге игнорируются null-значения,
 *   что полезно при частичном обновлении сущностей.
 */
@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring"
)
public interface AdEntityMapper {

    /**
     * Преобразует сущность {@link AdEntity} в базовый DTO {@link Ad}.
     * Маппит идентификатор автора из связи {@code author.id} в поле {@code author} DTO.
     *
     * @param entity исходная сущность объявления
     * @return DTO-представление объявления
     */
    @Mapping(source = "author.id", target = "author")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "image", source = "image")
    Ad toDto(AdEntity entity);

    /**
     * Преобразует сущность {@link AdEntity} в расширенный DTO {@link ExtendedAd}.
     * Извлекает детали об авторе (имя, фамилия, email, телефон) из связанной сущности {@link UserEntity}.
     *
     * @param adEntity исходная сущность объявления
     * @return расширенное DTO-представление объявления с данными об авторе
     */
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.username", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    ExtendedAd toExtendedAd(AdEntity adEntity);

    /**
     * Создает новую сущность {@link AdEntity} на основе данных из {@link CreateOrUpdateAd},
     * а также переданных изображения и автора.
     *
     * @param properties данные объявления (заголовок, описание, цена)
     * @param image      ссылка на изображение объявления
     * @param author     сущность автора объявления
     * @return новая сущность объявления, готовая к сохранению в БД
     */
    @Mapping(source = "author", target = "author")
    @Mapping(source = "image", target = "image")
    AdEntity createAdEntity(CreateOrUpdateAd properties, String image, UserEntity author);

    /**
     * Обновляет существующую сущность {@link AdEntity} данными из {@link CreateOrUpdateAd}.
     * Использует {@code @MappingTarget} для модификации переданного экземпляра сущности.
     * Благодаря настройке {@code NullValuePropertyMappingStrategy.IGNORE},
     * поля со значением null не перезаписываются.
     *
     * @param updateAd  данные для обновления объявления
     * @param adEntity  сущность, подлежащая обновлению
     */
    void updateAdEntity(CreateOrUpdateAd updateAd, @MappingTarget AdEntity adEntity);
}
