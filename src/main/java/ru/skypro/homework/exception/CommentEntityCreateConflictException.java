package ru.skypro.homework.exception;

/**
 * Исключение, сигнализирующее о том, что пользователь пытается выполнить операцию над несуществующим комментарием.
 * Наследуется от RuntimeException, так как является непроверяемым исключением.
 * Отличается от CommentEntityNotFoundException тем, что выбрасывается при попытке доступа к действию над комментарием,
 * когда комментарий еще не создан.
 */
public class CommentEntityCreateConflictException extends RuntimeException {

    /**
     * Конструктор для создания исключения с пользовательским сообщением.
     *
     * @param message подробное описание причины конфликта
     */
    public CommentEntityCreateConflictException(String message) {
        super(message);
    }
}
