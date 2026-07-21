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

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel = "spring")
public interface AdEntityMapper {

    @Mapping(source = "author.id", target = "author")
    Ad toDto(AdEntity entity);

    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.username", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    ExtendedAd toExtendedAd(AdEntity adEntity);

    @Mapping(source = "author", target = "author")
    @Mapping(source = "image", target = "image")
    AdEntity createAdEntity(CreateOrUpdateAd properties, String image, UserEntity author);

    void updateAdEntity(CreateOrUpdateAd updateAd, @MappingTarget AdEntity adEntity);
}
