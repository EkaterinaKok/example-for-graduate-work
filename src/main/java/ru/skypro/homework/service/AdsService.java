package ru.skypro.homework.service;


import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;

public interface AdsService {

    /**
     * Получение всех объявлений (для админов или всех пользователей)
     */
    Ads getAllAds();

    /**
     * Получение объявления по ID
     */
    Ad getAdById(Integer id);

    /**
     * Добавление объявления.
     * authorId - ID пользователя, который создает объявление (берем из SecurityContext)
     * image - файл картинки
     */
    Ad addAd(CreateOrUpdateAd dto, Integer authorId, MultipartFile image);

    /**
     * Удаление объявления по ID.
     * Обычно проверяют, что удаляет именно автор или админ.
     */
    void removeAd(Integer id, Integer currentUserId);

    /**
     * Обновление объявления (Patch).
     * Меняем только переданные поля.
     */
    Ad updateAd(Integer id, CreateOrUpdateAd dto, Integer currentUserId);

    /**
     * Получение объявлений текущего авторизованного пользователя (/ads/me)
     */
     Ads getAdsByAuthor(Integer authorId);

    /**
     * Обновление картинки объявления (/ads/{id}/image)
     */
    Ad updateImage(Integer id, MultipartFile image, Integer currentUserId);
}
