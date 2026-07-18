package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.ImageUploadException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.security.AuthUtils;
import ru.skypro.homework.service.UsersService;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final AuthUtils authUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ImageStorageServiceImpl imageStorageService;

    @Override
    public void setPassword(NewPassword dto) {
        Integer currentUserId = authUtils.getCurrentUserId();

        UserEntity user = usersRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Пользователь не найден"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(BAD_REQUEST, "Неверный текущий пароль");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        usersRepository.save(user);
    }

    @Override
    public User getUser() {
        Integer currentUserId = authUtils.getCurrentUserId();

        UserEntity user = usersRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Пользователь не найден"));

        return userMapper.toDto(user);
    }

    @Override
    public void updateUser(UpdateUser dto) {
        Integer currentUserId = authUtils.getCurrentUserId();

        UserEntity user = usersRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Пользователь не найден"));

        userMapper.updateFromDto(dto, user);

    }

    @Override
    public void updateUserImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Файл изображения не предоставлен или пуст");
        }

        Integer currentUserId = authUtils.getCurrentUserId();
        UserEntity user = usersRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Пользователь не найден"));

        try {

            String originalFilename = image.getOriginalFilename();
            byte[] fileBytes = image.getBytes();
            String imagePath = imageStorageService.saveImage(fileBytes, originalFilename);
            user.setImage(imagePath);
            usersRepository.save(user);

        } catch (IOException e) {
            log.error("Ошибка загрузки аватара", e);
            throw new ImageUploadException("Не удалось прочитать файл", e);
        } catch (ImageUploadException e) {
            log.error("Ошибка загрузки аватара", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Ошибка сохранения файла аватара", e);
        }
    }

}
