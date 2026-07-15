package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.security.AuthUtils;
import ru.skypro.homework.service.UsersService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final AuthUtils authUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void setPassword(NewPassword dto) {
        Integer currentUserId = authUtils.getCurrentUserId();

        UserEntity user = usersRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Пользователь не найден"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Неверный текущий пароль");
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

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }

    }

    @Override
    public void updateUserImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Файл изображения не предоставлен или пуст");
        }

        Integer currentUserId = authUtils.getCurrentUserId();

        UserEntity user = usersRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Пользователь не найден"));

        try {
            // 1. Создаем папку для загрузки, если её нет
            Path uploadDir = Paths.get("uploads", "images");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            image.transferTo(filePath);
            user.setImage("/images/" + fileName);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка сохранения файла аватара", e);
        }
    }

}
