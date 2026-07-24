package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserEntityMapper;
import ru.skypro.homework.repository.UserEntityRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Реализация сервиса аутентификации и регистрации пользователей.
 * Предоставляет методы для проверки учетных данных при входе в систему и создания новых пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

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
     * Маппер для преобразования DTO регистрации пользователя в сущность {@link UserEntity}.
     */
    private final UserEntityMapper userEntityMapper;

    /**
     * Выполняет проверку учетных данных пользователя для аутентификации.
     * Ищет пользователя по логину и проверяет соответствие переданного пароля хешированному значению в базе данных.
     *
     * @param userName логин пользователя
     * @param password пароль пользователя в открытом виде
     * @return {@code true}, если логин и пароль совпадают с данными в БД, иначе {@code false}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean login(String userName, String password) {
        return userEntityRepository.findByUsername(userName)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    /**
     * Регистрирует нового пользователя в системе.
     * Проверяет уникальность логина, создает сущность пользователя, хеширует пароль и сохраняет запись в БД.
     *
     * @param register данные для регистрации пользователя из DTO {@link Register}
     * @return {@code true}, если регистрация прошла успешно, {@code false}, если пользователь с таким логином уже существует
     */
    @Transactional
    @Override
    public boolean register(Register register) {
        if (userEntityRepository.findByUsername(register.getUsername()).isPresent()) {
            return false;
        }

        UserEntity userEntity = userEntityMapper.registerUser(register);
        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));
        userEntityRepository.save(userEntity);

        return true;
    }
}
