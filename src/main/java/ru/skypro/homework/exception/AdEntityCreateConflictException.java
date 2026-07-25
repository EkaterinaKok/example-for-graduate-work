package ru.skypro.homework.exception;

/**
 * Исключение, сигнализирующее о том, что пользователь пытается выполнить операцию над несуществующим объявлением.
 * Наследуется от RuntimeException, так как является непроверяемым исключением.
 * Отличается от AdEntityNotFoundException тем, что выбрасывается при попытке доступа к действию над объявлением,
 * когда объявление еще не создано (например, при проверке прав на несуществующее объявление).
 */
public class AdEntityCreateConflictException extends RuntimeException {

    /**
     * Конструктор для создания исключения с пользовательским сообщением.
     *
     * @param message подробное описание причины конфликта
     */
    public AdEntityCreateConflictException(String message) {
        super(message);
    }
}
