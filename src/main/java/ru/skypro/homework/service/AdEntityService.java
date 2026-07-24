package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.io.IOException;

/**
 * Интерфейс сервиса для управления объявлениями.
 * Определяет контракт для операций получения, создания, обновления, удаления объявлений,
 * работы с изображениями и проверки прав владельца.
 */
public interface AdEntityService {

    /**
     * Получает список всех объявлений в системе.
     *
     * @return объект {@link Ads}, содержащий список объявлений и их общее количество
     */
    Ads getAllAds();

    /**
     * Создает новое объявление.
     *
     * @param properties      данные объявления (заголовок, описание, цена) из DTO {@link CreateOrUpdateAd}
     * @param image           загруженный файл изображения в формате {@link MultipartFile}
     * @param authentication  объект аутентификации для определения автора объявления
     * @return DTO созданного объявления {@link Ad}
     * @throws IOException если произошла ошибка при сохранении файла изображения
     */
    Ad addAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException;

    /**
     * Получает расширенные данные об объявлении по его идентификатору.
     *
     * @param id              уникальный идентификатор объявления
     * @param authentication  объект аутентификации (может использоваться для проверки прав доступа)
     * @return расширенное DTO объявления {@link ExtendedAd}
     */
    ExtendedAd getAds(int id, Authentication authentication);

    /**
     * Удаляет объявление по его идентификатору.
     * Также инициирует удаление связанного изображения.
     *
     * @param id идентификатор удаляемого объявления
     * @throws IOException если произошла ошибка при удалении файла изображения
     */
    void removeAd(int id) throws IOException;

    /**
     * Обновляет данные существующего объявления.
     *
     * @param id        идентификатор обновляемого объявления
     * @param updateAd  новые данные для обновления объявления из DTO {@link CreateOrUpdateAd}
     * @return обновленное DTO объявления {@link Ad}
     */
    Ad updateAds(int id, CreateOrUpdateAd updateAd);

    /**
     * Получает список объявлений, принадлежащих текущему авторизованному пользователю.
     *
     * @param authentication объект аутентификации для идентификации пользователя
     * @return объект {@link Ads} со списком личных объявлений пользователя и их количеством
     */
    Ads getAdsMe(Authentication authentication);

    /**
     * Обновляет изображение объявления.
     * Сохраняет новый файл, обновляет ссылку в сущности и удаляет старое изображение.
     *
     * @param id             идентификатор объявления
     * @param image          новый файл изображения в формате {@link MultipartFile}
     * @param authentication объект аутентификации (используется для проверки прав владельца)
     * @return массив байтов с содержимым нового изображения
     * @throws IOException если произошла ошибка при работе с файлами изображений
     */
    byte[] updateImage(int id, MultipartFile image, Authentication authentication) throws IOException;

    /**
     * Проверяет, является ли указанный пользователь владельцем объявления.
     *
     * @param username имя пользователя (логин) для проверки
     * @param id       идентификатор объявления
     * @return {@code true}, если пользователь является владельцем объявления, иначе {@code false}
     */
    boolean isOwner(String username, int id);
}
