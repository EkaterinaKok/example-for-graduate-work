package ru.skypro.homework.exception;

/**
 * Исключение, сигнализирующее о том, что сущность пользователя (UserEntity) не найдена в базе данных.
 * Наследуется от RuntimeException, так как является непроверяемым исключением.
 * Обычно выбрасывается в сервисах или репозиториях при попытке получить данные несуществующего пользователя.
 */
public class UserEntityNotFoundException extends RuntimeException {

    /**
     * Конструктор для создания исключения с пользовательским сообщением.
     *
     * @param message подробное описание причины отсутствия пользователя
     */
    public UserEntityNotFoundException(String message) {
        super(message);
    }
}
