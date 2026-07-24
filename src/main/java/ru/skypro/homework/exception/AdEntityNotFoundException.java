package ru.skypro.homework.exception;

/**
 * Исключение, сигнализирующее о том, что сущность объявления (AdEntity) не найдена в базе данных.
 * Наследуется от RuntimeException, так как является непроверяемым исключением.
 * Обычно выбрасывается в сервисах или репозиториях, когда запрашиваемое объявление отсутствует.
 */
public class AdEntityNotFoundException extends RuntimeException {

    /**
     * Конструктор для создания исключения с пользовательским сообщением.
     *
     * @param message подробное описание причины отсутствия объявления
     */
    public AdEntityNotFoundException(String message) {
        super(message);
    }
}
