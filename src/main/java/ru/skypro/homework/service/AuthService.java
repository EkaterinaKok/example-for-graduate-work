package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

/**
 * Интерфейс сервиса аутентификации и регистрации пользователей.
 * Определяет базовые операции для входа в систему и создания новых учетных записей.
 */
public interface AuthService {

    /**
     * Выполняет проверку учетных данных пользователя для аутентификации.
     * Сравнивает логин и пароль с данными, хранящимися в базе данных.
     *
     * @param userName логин пользователя
     * @param password пароль пользователя в открытом виде
     * @return {@code true}, если учетные данные верны, иначе {@code false}
     */
    boolean login(String userName, String password);

    /**
     * Регистрирует нового пользователя в системе.
     * Проверяет уникальность логина и создает новую учетную запись.
     *
     * @param register данные для регистрации пользователя из DTO {@link Register}
     * @return {@code true}, если регистрация прошла успешно, {@code false}, если пользователь с таким логином уже существует
     */
    boolean register(Register register);
}
