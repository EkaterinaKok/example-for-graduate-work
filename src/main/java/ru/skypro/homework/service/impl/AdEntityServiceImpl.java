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

/**
 * Реализация сервиса для управления объявлениями ({@link AdEntity}).
 * Предоставляет бизнес-логику для создания, чтения, обновления и удаления объявлений,
 * а также работы с изображениями и проверки прав доступа.
 * Все методы, изменяющие состояние базы данных, помечены аннотацией {@link Transactional}.
 */
@RequiredArgsConstructor
@Service
public class AdEntityServiceImpl implements AdEntityService {

    /**
     * Репозиторий для доступа к данным объявлений в базе данных.
     */
    private final AdEntityRepository adEntityRepository;

    /**
     * Маппер для преобразования сущностей объявлений ({@link AdEntity}) в DTO и обратно.
     */
    private final AdEntityMapper adEntityMapper;

    /**
     * Репозиторий для доступа к данным пользователей в базе данных.
     * Используется для получения информации об авторе объявления.
     */
    private final UserEntityRepository userEntityRepository;

    /**
     * Сервис для работы с изображениями объявлений.
     * Обеспечивает сохранение, удаление и получение файлов изображений.
     */
    private final ImageService imageService;

    /**
     * Получает список всех активных объявлений из базы данных.
     * Преобразует найденные сущности в DTO-объекты и формирует итоговый ответ {@link Ads},
     * содержащий список объявлений и их общее количество.
     *
     * @return объект {@link Ads} со списком всех объявлений и их количеством
     */
    @Transactional(readOnly = true)
    @Override
    public Ads getAllAds() {
        List<Ad> results = adEntityRepository.findAll().stream().map(adEntityMapper::toDto).toList();

        Ads ads = new Ads();
        ads.setResults(results);
        ads.setCount(results.size());
        return ads;
    }

    /**
     * Создает новое объявление на основе переданных данных.
     * Определяет автора объявления по объекту аутентификации, сохраняет загруженное изображение,
     * формирует сущность объявления с помощью маппера и сохраняет её в базу данных.
     *
     * @param properties      данные объявления (заголовок, описание, цена) из DTO {@link CreateOrUpdateAd}
     * @param image           загруженный файл изображения в формате {@link MultipartFile}
     * @param authentication  объект аутентификации, содержащий данные текущего пользователя
     * @return DTO созданного объявления {@link Ad}
     * @throws IOException                если произошла ошибка при сохранении файла изображения
     * @throws UserEntityNotFoundException если пользователь, указанный как автор, не найден в базе данных
     */
    @Transactional
    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException {
        UserEntity userEntity = userEntityRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserEntityNotFoundException("Пользователь не найден"));

        String imagePath = imageService.saveImage(image);
        AdEntity adEntity = adEntityMapper.createAdEntity(properties, imagePath, userEntity);
        AdEntity savedAdEntity = adEntityRepository.save(adEntity);

        return adEntityMapper.toDto(savedAdEntity);
    }

    /**
     * Получает расширенные данные об объявлении по его уникальному идентификатору.
     * Включает в ответ подробную информацию не только об объявлении, но и об его авторе.
     *
     * @param id              уникальный идентификатор объявления
     * @param authentication  объект аутентификации (может использоваться для проверки прав доступа в будущем)
     * @return расширенное DTO объявления {@link ExtendedAd}
     * @throws AdEntityNotFoundException если объявление с указанным ID не найдено в базе данных
     */
    @Transactional(readOnly = true)
    @Override
    public ExtendedAd getAds(int id, Authentication authentication) {
        AdEntity adEntity = adEntityRepository.findById(id).orElseThrow(() -> new AdEntityNotFoundException("Объявление не найдено"));

        return adEntityMapper.toExtendedAd(adEntity);
    }

    /**
     * Удаляет объявление из базы данных по его идентификатору.
     * Перед удалением записи из БД удаляет связанное с объявлением изображение из файлового хранилища.
     *
     * @param id идентификатор удаляемого объявления
     * @throws IOException            если произошла ошибка при удалении файла изображения
     * @throws AdEntityNotFoundException если объявление с указанным ID не найдено
     */
    @Transactional
    @Override
    public void removeAd(int id) throws IOException {
        AdEntity adEntity = adEntityRepository.findById(id).orElseThrow(() -> new AdEntityNotFoundException("Объявление не найдено"));
        String adEntityImage = adEntity.getImage();

        adEntityRepository.delete(adEntity);
        imageService.deleteImage(adEntityImage);
    }

    /**
     * Обновляет данные существующего объявления.
     * Находит объявление по ID, применяет изменения с помощью маппера, сохраняет обновленную сущность в БД
     * и возвращает актуальное DTO-представление.
     *
     * @param id        идентификатор обновляемого объявления
     * @param updateAd  новые данные для обновления объявления из DTO {@link CreateOrUpdateAd}
     * @return обновленное DTO объявления {@link Ad}
     * @throws AdEntityNotFoundException если объявление с указанным ID не найдено
     */
    @Transactional
    @Override
    public Ad updateAds(int id, CreateOrUpdateAd updateAd) {
        AdEntity adEntity = adEntityRepository.findById(id).orElseThrow(() -> new AdEntityNotFoundException("Объявление не найдено"));

        adEntityMapper.updateAdEntity(updateAd, adEntity);
        adEntityRepository.save(adEntity);

        return adEntityMapper.toDto(adEntity);
    }

    /**
     * Получает список объявлений, принадлежащих текущему авторизованному пользователю.
     * Формирует ответ {@link Ads}, содержащий список личных объявлений и их количество.
     *
     * @param authentication объект аутентификации для идентификации текущего пользователя
     * @return объект {@link Ads} со списком объявлений пользователя и их количеством
     * @throws UserEntityNotFoundException если пользователь не найден в базе данных
     */
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

    /**
     * Обновляет изображение для существующего объявления.
     * Сохраняет новый файл изображения, обновляет ссылку в сущности объявления, удаляет старое изображение
     * из файлового хранилища и возвращает содержимое нового изображения в виде массива байтов.
     *
     * @param id             идентификатор объявления, для которого обновляется изображение
     * @param image          новый файл изображения в формате {@link MultipartFile}
     * @param authentication объект аутентификации (используется для проверки прав владельца)
     * @return массив байтов, содержащий содержимое нового изображения
     * @throws IOException            если произошла ошибка при работе с файлами изображений
     * @throws AdEntityNotFoundException если объявление с указанным ID не найдено
     */
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

    /**
     * Проверяет, является ли указанный пользователь владельцем объявления с заданным ID.
     * Возвращает {@code true}, если пользователь совпадает с автором объявления, и {@code false} в противном случае.
     * <p>
     * Примечание: в текущей реализации при отсутствии объявления метод возвращает {@code true}.
     * Это может быть потенциальной логической ошибкой и требует дополнительного анализа требований.
     *
     * @param username имя пользователя (логин) для проверки
     * @param id       идентификатор объявления
     * @return {@code true}, если пользователь является владельцем объявления, иначе {@code false}
     */
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
