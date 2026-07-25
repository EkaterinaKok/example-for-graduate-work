package ru.skypro.homework.exception;

/**
 * Исключение, сигнализирующее о неверном текущем пароле при попытке его изменения.
 * Наследуется от RuntimeException, так как является непроверяемым исключением.
 * Обычно выбрасывается в UserEntityServiceImpl при несовпадении текущего пароля с хешированным значением в БД.
 */
public class InvalidCurrentPasswordException extends RuntimeException {

    /**
     * Конструктор для создания исключения с пользовательским сообщением.
     *
     * @param message подробное описание причины ошибки (например, "Текущий пароль неверен")
     */
    public InvalidCurrentPasswordException(String message) {
        super(message);
    }
}
