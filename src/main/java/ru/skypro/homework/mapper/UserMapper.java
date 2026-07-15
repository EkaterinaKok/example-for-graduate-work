package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Маппинг Entity -> DTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "image", source = "image")
    User toDto(UserEntity entity);

    // Если понадобится обновление (редко для User, но полезно иметь)
    void updateFromDto(ru.skypro.homework.dto.UpdateUser dto, @org.mapstruct.MappingTarget ru.skypro.homework.entity.UserEntity entity);
}
