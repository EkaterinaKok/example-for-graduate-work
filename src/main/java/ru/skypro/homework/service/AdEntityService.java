package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.io.IOException;

public interface AdEntityService {

    Ads getAllAds();

    Ad addAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException;

    ExtendedAd getAds(int id, Authentication authentication);

    void removeAd(int id) throws IOException;

    Ad updateAds(int id, CreateOrUpdateAd updateAd);

    Ads getAdsMe(Authentication authentication);

    byte[] updateImage(int id, MultipartFile image, Authentication authentication) throws IOException;

    boolean isOwner(String username, int id);
}
