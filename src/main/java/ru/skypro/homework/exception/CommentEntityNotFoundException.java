package ru.skypro.homework.exception;

/**
 * Исключение, сигнализирующее о том, что сущность комментария (CommentEntity) не найдена в базе данных.
 * Наследуется от RuntimeException, так как является непроверяемым исключением.
 * Обычно выбрасывается в сервисах или репозиториях при попытке получить или изменить несуществующий комментарий.
 */
public class CommentEntityNotFoundException extends RuntimeException {

    /**
     * Конструктор для создания исключения с пользовательским сообщением.
     *
     * @param message подробное описание причины отсутствия комментария
     */
    public CommentEntityNotFoundException(String message) {
        super(message);
    }
}
