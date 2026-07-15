package ru.skypro.homework.service;


import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

public interface AdsService {

    Ads getAllAds();

    Ad getAdById(Integer id);

    Ad addAd(CreateOrUpdateAd dto, Integer authorId, MultipartFile image);

    void removeAd(Integer id, Integer currentUserId);

    Ad updateAd(Integer id, CreateOrUpdateAd dto, Integer currentUserId);

    Ads getAdsByAuthor(Integer authorId);

    Ad updateImage(Integer id, MultipartFile image, Integer currentUserId);

    ExtendedAd getExtendedAdById(Integer id);
}

