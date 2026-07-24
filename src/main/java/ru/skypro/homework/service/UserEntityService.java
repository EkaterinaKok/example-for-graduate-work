package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

import java.io.IOException;

/**
 * Интерфейс сервиса для управления данными пользователей.
 * Определяет контракт для смены пароля, получения профиля, обновления личных данных
 * и загрузки аватара пользователя.
 */
public interface UserEntityService {

    /**
     * Изменяет пароль пользователя после проверки текущего пароля.
     *
     * @param newPassword     данные для смены пароля из DTO {@link NewPassword}
     * @param authentication  объект аутентификации для идентификации пользователя
     * @throws org.springframework.security.access.AccessDeniedException если текущий пароль неверен
     */
    void setPassword(NewPassword newPassword, Authentication authentication);

    /**
     * Получает данные профиля текущего авторизованного пользователя.
     *
     * @param authentication объект аутентификации для идентификации пользователя
     * @return DTO профиля пользователя {@link User}
     */
    User getUser(Authentication authentication);

    /**
     * Обновляет личные данные пользователя (имя, фамилия, телефон и т.д.).
     *
     * @param updateUser     новые данные пользователя для обновления из DTO {@link UpdateUser}
     * @param authentication объект аутентификации для идентификации пользователя
     * @return DTO с переданными данными обновления {@link UpdateUser}
     */
    UpdateUser updateUser(UpdateUser updateUser, Authentication authentication);

    /**
     * Обновляет аватар пользователя.
     * Сохраняет новое изображение, обновляет ссылку в сущности пользователя,
     * а старое изображение удаляет асинхронно.
     *
     * @param image          новый файл изображения аватара в формате {@link MultipartFile}
     * @param authentication объект аутентификации для идентификации пользователя
     * @throws IOException если произошла ошибка при работе с файлами изображений
     */
    void updateUserImage(MultipartFile image, Authentication authentication) throws IOException;
}