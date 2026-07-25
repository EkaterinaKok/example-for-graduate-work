package ru.skypro.homework.exception;

/**
 * Исключение, сигнализирующее о том, что изображение пользователя не найдено в файловой системе.
 * Наследуется от RuntimeException, так как является непроверяемым исключением.
 * Обычно выбрасывается при попытке удаления несуществующего аватара.
 */
public class ImageNotFoundException extends RuntimeException {

    /**
     * Конструктор для создания исключения с пользовательским сообщением.
     *
     * @param message подробное описание причины отсутствия изображения
     */
    public ImageNotFoundException(String message) {
        super(message);
    }
}
