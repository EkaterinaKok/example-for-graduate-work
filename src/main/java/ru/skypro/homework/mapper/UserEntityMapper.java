package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel = "spring")
public interface UserEntityMapper {

    @Mapping(source = "username", target = "email")
    @Mapping(target = "role", expression = "java(ru.skypro.homework.dto.Role.valueOf(entity.getAuthority().replace(\"ROLE_\",\"\")))")
    User toDTO(UserEntity entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authority", expression = "java(\"ROLE_\" + register.getRole())")
    UserEntity registerUser(Register register);

    void updateUserEntity(UpdateUser updateUser, @MappingTarget UserEntity userEntity);
}