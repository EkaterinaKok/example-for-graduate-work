package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

/**
 * MapStruct-интерфейс для маппинга сущностей пользователей (UserEntity) и DTO.
 * Обеспечивает преобразование между {@link UserEntity} и {@link User},
 * создание новой сущности при регистрации ({@link Register}),
 * а также частичное обновление данных пользователя ({@link UpdateUser}).
 *
 * Конфигурация:
 * - {@code componentModel = "spring"}: генерируемый маппер регистрируется как Spring-бин.
 * - {@code nullValuePropertyMappingStrategy = IGNORE}: при обновлении игнорируются null-поля,
 *   позволяя обновлять только переданные атрибуты.
 */
@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring"
)
public interface UserEntityMapper {

    /**
     * Преобразует сущность {@link UserEntity} в DTO {@link User}.
     * Выполняет дополнительные преобразования:
     * - поле {@code username} сущности маппится в поле {@code email} DTO;
     * - поле {@code authority} (например, "ROLE_USER") преобразуется в {@link ru.skypro.homework.dto.Role}
     *   путем удаления префикса "ROLE_" и приведения к enum.
     *
     * @param entity исходная сущность пользователя
     * @return DTO-представление пользователя
     */
    @Mapping(source = "username", target = "email")
    @Mapping(
            target = "role",
            expression = "java(ru.skypro.homework.dto.Role.valueOf(entity.getAuthority().replace(\"ROLE_\",\"\")))"
    )
    User toDTO(UserEntity entity);

    /**
     * Создает новую сущность {@link UserEntity} на основе данных регистрации {@link Register}.
     * Выполняет следующие преобразования:
     * - игнорирует поле {@code password} в результирующей сущности (предполагается, что оно будет
     *   установлено отдельно после хеширования);
     * - формирует значение поля {@code authority}, добавляя префикс "ROLE_" к значению роли из DTO.
     *
     * @param register данные для регистрации пользователя
     * @return новая сущность пользователя, готовая для сохранения (без установленного пароля)
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(
            target = "authority",
            expression = "java(\"ROLE_\" + register.getRole())"
    )
    UserEntity registerUser(Register register);

    /**
     * Частично обновляет существующую сущность {@link UserEntity} данными из {@link UpdateUser}.
     * Использует {@code @MappingTarget} для модификации переданного экземпляра сущности.
     * Поля со значением null игнорируются благодаря настройке маппера, что позволяет
     * обновлять только те атрибуты, которые были переданы в запросе.
     *
     * @param updateUser данные для обновления профиля пользователя (имя, фамилия, телефон)
     * @param userEntity сущность, подлежащая обновлению
     */
    void updateUserEntity(UpdateUser updateUser, @MappingTarget UserEntity userEntity);
}