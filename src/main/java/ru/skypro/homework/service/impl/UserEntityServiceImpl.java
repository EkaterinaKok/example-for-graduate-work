package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.UserEntityNotFoundException;
import ru.skypro.homework.mapper.UserEntityMapper;
import ru.skypro.homework.repository.UserEntityRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserEntityService;

import java.io.IOException;

/**
 * Реализация сервиса для управления данными пользователей.
 * Предоставляет функционал для смены пароля, получения профиля, обновления личных данных
 * и загрузки аватара пользователя.
 */
@Service
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService {

    /**
     * Репозиторий для доступа к данным пользователей в базе данных.
     */
    private final UserEntityRepository userEntityRepository;

    /**
     * Компонент для хеширования паролей и проверки их соответствия.
     * Гарантирует безопасное хранение паролей в зашифрованном виде.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Маппер для преобразования сущностей пользователей ({@link UserEntity}) в DTO и обратно.
     */
    private final UserEntityMapper userEntityMapper;

    /**
     * Сервис для работы с файлами изображений.
     * Используется для сохранения и удаления аватаров пользователей.
     */
    private final ImageService imageService;

    /**
     * Изменяет пароль пользователя после проверки текущего пароля.
     * Сравнивает переданный текущий пароль с хешированным значением в БД,
     * хеширует новый пароль и сохраняет обновленную сущность.
     *
     * @param newPassword     данные для смены пароля из DTO {@link NewPassword}
     * @param authentication  объект аутентификации для идентификации пользователя
     * @throws AccessDeniedException      если текущий пароль неверен
     * @throws UserEntityNotFoundException если пользователь не найден в базе данных
     */
    @Transactional
    @Override
    public void setPassword(NewPassword newPassword, Authentication authentication) {
        UserEntity userEntity = userEntityRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserEntityNotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())) {
            throw new AccessDeniedException("Текущий пароль неверен");
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userEntityRepository.save(userEntity);
    }

    /**
     * Получает данные профиля текущего авторизованного пользователя.
     * Преобразует сущность пользователя в DTO-представление.
     *
     * @param authentication объект аутентификации для идентификации пользователя
     * @return DTO профиля пользователя {@link User}
     * @throws UserEntityNotFoundException если пользователь не найден в базе данных
     */
    @Transactional(readOnly = true)
    @Override
    public User getUser(Authentication authentication) {
        UserEntity userEntity = userEntityRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserEntityNotFoundException("Пользователь не найден"));

        return userEntityMapper.toDTO(userEntity);
    }

    /**
     * Обновляет личные данные пользователя (имя, фамилия, телефон и т.д.).
     * Применяет изменения к сущности пользователя с помощью маппера и сохраняет обновленные данные в БД.
     *
     * @param updateUser     новые данные пользователя для обновления из DTO {@link UpdateUser}
     * @param authentication объект аутентификации для идентификации пользователя
     * @return DTO с переданными данными обновления {@link UpdateUser}
     * @throws UserEntityNotFoundException если пользователь не найден в базе данных
     */
    @Transactional
    @Override
    public UpdateUser updateUser(UpdateUser updateUser, Authentication authentication) {
        UserEntity userEntity = userEntityRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserEntityNotFoundException("Пользователь не найден"));

        userEntityMapper.updateUserEntity(updateUser, userEntity);
        userEntityRepository.save(userEntity);

        return updateUser;
    }

    /**
     * Обновляет аватар пользователя.
     * Сохраняет новое изображение, обновляет ссылку в сущности пользователя,
     * а старое изображение удаляет асинхронно через отдельный поток с небольшой задержкой.
     *
     * @param image          новый файл изображения аватара в формате {@link MultipartFile}
     * @param authentication объект аутентификации для идентификации пользователя
     * @throws IOException            если произошла ошибка при работе с файлами изображений
     * @throws UserEntityNotFoundException если пользователь не найден в базе данных
     */
    @Transactional
    @Override
    public void updateUserImage(MultipartFile image, Authentication authentication) throws IOException {
        UserEntity userEntity = userEntityRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserEntityNotFoundException("Пользователь не найден"));
        String userEntityImage = userEntity.getImage();

        String imagePath = imageService.saveImage(image);
        userEntity.setImage(imagePath);
        userEntityRepository.save(userEntity);

        // Асинхронное удаление старого изображения с задержкой 800 мс
        if (userEntityImage != null) {
            new Thread(() -> {
                try {
                    Thread.sleep(800);
                    imageService.deleteImage(userEntityImage);
                } catch (IOException | InterruptedException ignored) {
                }
            }).start();
        }
    }
}
