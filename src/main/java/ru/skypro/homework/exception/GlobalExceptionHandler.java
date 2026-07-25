package ru.skypro.homework.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skypro.homework.dto.ErrorResponseDTO;

/**
 * Глобальный обработчик исключений для REST-контроллеров приложения.
 * Перехватывает различные типы исключений и преобразует их в структурированные HTTP-ответы
 * с использованием DTO {@link ErrorResponseDTO}.
 * Логирует ошибки с соответствующим уровнем важности (warn для ожидаемых ошибок, error для непредвиденных).
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработчик для исключения отсутствия пользователя.
     * Возвращает HTTP-статус 404 (Not Found).
     */
    @ExceptionHandler(UserEntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserEntityNotFound(UserEntityNotFoundException e, HttpServletRequest request) {
        log.warn("User not found: {}", e.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, "UserEntity Not Found", e.getMessage(), request.getRequestURI());
    }

    /**
     * Обработчик для исключения отсутствия объявления.
     * Возвращает HTTP-статус 404 (Not Found).
     */
    @ExceptionHandler(AdEntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAdEntityNotFound(AdEntityNotFoundException e, HttpServletRequest request) {
        log.warn("Ad not found: {}", e.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, "AdEntity Not Found", e.getMessage(), request.getRequestURI());
    }

    /**
     * Обработчик для исключения отсутствия комментария.
     * Возвращает HTTP-статус 404 (Not Found).
     */
    @ExceptionHandler(CommentEntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCommentEntityNotFound(CommentEntityNotFoundException e, HttpServletRequest request) {
        log.warn("Comment not found: {}", e.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, "CommentEntity Not Found", e.getMessage(), request.getRequestURI());
    }

    /**
     * Обработчик ошибок валидации входных данных (например, при нарушении аннотаций @Size, @NotBlank и т.д.).
     * Возвращает HTTP-статус 400 (Bad Request) с указанием поля и сообщения об ошибке.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null
                ? String.format("Field error '%s': %s", fieldError.getField(), fieldError.getDefaultMessage())
                : "Validation error";
        log.warn("Validation error: {}", message);
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", message, request.getRequestURI());
    }

    /**
     * Обработчик исключения отказа в доступе (AccessDeniedException).
     * Обычно возникает при проверке прав пользователя (например, попытка удалить чужое объявление).
     * Возвращает HTTP-статус 403 (Forbidden).
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access denied for request: URI={}, Message={}", request.getRequestURI(), ex.getMessage(), ex);
        String message = "You do not have sufficient rights to perform this action. You are not the owner of the ad.";
        return createErrorResponse(HttpStatus.FORBIDDEN, "Forbidden", message, request.getRequestURI());
    }

    /**
     * Универсальный обработчик для любых непредвиденных исключений.
     * Возвращает HTTP-статус 500 (Internal Server Error).
     * Скрывает детали внутренней ошибки от клиента, но подробно логирует их на сервере.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred", request.getRequestURI());
    }

    /**
     * Вспомогательный метод для создания унифицированного объекта ответа об ошибке.
     * Инициализирует {@link ErrorResponseDTO} с текущей временной меткой и формирует ResponseEntity.
     *
     * @param status  HTTP-статус ошибки
     * @param error   краткое описание типа ошибки (например, "Not Found")
     * @param message подробное сообщение об ошибке
     * @param path    URI запроса, вызвавшего ошибку
     * @return ResponseEntity с объектом ErrorResponseDTO
     */
    private ResponseEntity<ErrorResponseDTO> createErrorResponse(HttpStatus status, String error, String message, String path) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(status.value(), error, message, path);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
