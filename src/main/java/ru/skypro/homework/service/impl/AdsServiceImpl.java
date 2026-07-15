package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AdsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final UsersRepository usersRepository;
    private final AdMapper adMapper;

    @Override
    public Ads getAllAds() {
        List<AdEntity> allAds = adsRepository.findAll();
        List<Ad> dtoList = allAds.stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        Ads result = new Ads();
        result.setCount(allAds.size());
        result.setResults(dtoList);
        return result;
    }

    @Override
    public Ad getAdById(Integer id) {
        AdEntity ad = adsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        return adMapper.toDto(ad);
    }

    @Override
    public Ad addAd(CreateOrUpdateAd dto, Integer authorId, MultipartFile image) {
        UserEntity author = usersRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        AdEntity adEntity = adMapper.toEntity(dto);

        adEntity.setAuthor(author);
        adEntity.setCreatedAt(LocalDateTime.now());

        if (image != null && !image.isEmpty()) {
            String path = "images/" + image.getOriginalFilename();
            adEntity.setImage(path);
        }

        AdEntity saved = adsRepository.save(adEntity);
        return adMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void removeAd(Integer id, Integer currentUserId) {
        AdEntity ad = adsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        if (!ad.getAuthor().getId().equals(currentUserId)) {
            throw new RuntimeException("Нельзя удалять чужие объявления");
        }

        adsRepository.delete(ad);
    }

    @Override
    public Ad updateAd(Integer id, CreateOrUpdateAd dto, Integer currentUserId) {
        AdEntity ad = adsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        if (!ad.getAuthor().getId().equals(currentUserId)) {
            throw new RuntimeException("Нельзя обновлять чужие объявления");
        }

        if (dto.getTitle() != null) ad.setTitle(dto.getTitle());
        if (dto.getPrice() != null) ad.setPrice(dto.getPrice());
        if (dto.getDescription() != null) ad.setDescription(dto.getDescription());

        AdEntity updated = adsRepository.save(ad);
        return adMapper.toDto(updated);
    }

    @Override
    public Ads getAdsByAuthor(Integer authorId) {

        List<AdEntity> ads = adsRepository.findAllByAuthorId(authorId);

        List<Ad> dtoList = ads.stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        Ads result = new Ads();
        result.setCount(ads.size());
        result.setResults(dtoList);
        return result;
    }

    @Override
    public Ad updateImage(Integer id, MultipartFile image, Integer currentUserId) {
        AdEntity ad = adsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        if (image != null && !image.isEmpty()) {
            String path = "images/" + image.getOriginalFilename();
            ad.setImage(path);
            adsRepository.save(ad);
        }
        return adMapper.toDto(ad);
    }

    @Override
    public ExtendedAd getExtendedAdById(Integer id) {
        AdEntity ad = adsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        ExtendedAd dto = new ExtendedAd();
        dto.setPk(ad.getPk());
        dto.setTitle(ad.getTitle());
        dto.setPrice(ad.getPrice());
        dto.setDescription(ad.getDescription());
        dto.setImage(ad.getImage());

        UserEntity author = ad.getAuthor();
        if (author != null) {
            dto.setAuthorFirstName(author.getFirstName());
            dto.setAuthorLastName(author.getLastName());
            dto.setEmail(author.getEmail());
            dto.setPhone(author.getPhone());
        }

        return dto;
    }

}
