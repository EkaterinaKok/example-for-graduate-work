package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdEntityNotFoundException;
import ru.skypro.homework.exception.UserEntityNotFoundException;
import ru.skypro.homework.mapper.AdEntityMapper;
import ru.skypro.homework.repository.AdEntityRepository;
import ru.skypro.homework.repository.UserEntityRepository;
import ru.skypro.homework.service.AdEntityService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdEntityServiceImpl implements AdEntityService {

    private final AdEntityRepository adEntityRepository;

    private final AdEntityMapper adEntityMapper;

    private final UserEntityRepository userEntityRepository;

    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Override
    public Ads getAllAds() {
        List<Ad> results = adEntityRepository.findAll().stream().map(adEntityMapper::toDto).toList();

        Ads ads = new Ads();
        ads.setResults(results);
        ads.setCount(results.size());
        return ads;
    }

    @Transactional
    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException {
        UserEntity userEntity = userEntityRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserEntityNotFoundException("Пользователь не найден"));

        String imagePath = imageService.saveImage(image);
        AdEntity adEntity = adEntityMapper.createAdEntity(properties, imagePath, userEntity);
        AdEntity savedAdEntity = adEntityRepository.save(adEntity);

        return adEntityMapper.toDto(savedAdEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ExtendedAd getAds(int id, Authentication authentication) {
        AdEntity adEntity = adEntityRepository.findById(id).orElseThrow(() -> new AdEntityNotFoundException("Объявление не найдено"));

        return adEntityMapper.toExtendedAd(adEntity);
    }

    @Transactional
    @Override
    public void removeAd(int id) throws IOException {
        AdEntity adEntity = adEntityRepository.findById(id).orElseThrow(() -> new AdEntityNotFoundException("Объявление не найдено"));
        String adEntityImage = adEntity.getImage();

        adEntityRepository.delete(adEntity);
        imageService.deleteImage(adEntityImage);
    }

    @Transactional
    @Override
    public Ad updateAds(int id, CreateOrUpdateAd updateAd) {
        AdEntity adEntity = adEntityRepository.findById(id).orElseThrow(() -> new AdEntityNotFoundException("Объявление не найдено"));

        adEntityMapper.updateAdEntity(updateAd, adEntity);
        adEntityRepository.save(adEntity);

        return adEntityMapper.toDto(adEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Ads getAdsMe(Authentication authentication) {
        UserEntity userEntity = userEntityRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserEntityNotFoundException("Пользователь не найден"));
        List<Ad> results = userEntity.getAdEntities().stream().map(adEntityMapper::toDto).toList();

        Ads ads = new Ads();
        ads.setResults(results);
        ads.setCount(results.size());
        return ads;
    }

    @Transactional
    @Override
    public byte[] updateImage(int id, MultipartFile image, Authentication authentication) throws IOException {
        AdEntity adEntity = adEntityRepository.findById(id).orElseThrow(() -> new AdEntityNotFoundException("Объявление не найдено"));
        String oldAdEntityImage = adEntity.getImage();

        String imagePath = imageService.saveImage(image);
        adEntity.setImage(imagePath);
        adEntityRepository.save(adEntity);
        imageService.deleteImage(oldAdEntityImage);

        return imageService.getImage(imagePath);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isOwner(String username, int id) {
        Optional<AdEntity> byId = adEntityRepository.findById(id);

        if (byId.isEmpty()) {
            return true;
        }

        return byId
                .map(ad -> ad.getAuthor().getUsername().equals(username))
                .orElse(false);
    }
}
