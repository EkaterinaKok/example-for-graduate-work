package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Интерфейс сервиса для работы с изображениями.
 * Определяет базовый контракт для сохранения, чтения и удаления файлов изображений
 * в файловой системе приложения.
 */
public interface ImageService {

    /**
     * Сохраняет загруженный файл изображения в файловую систему.
     * Генерирует уникальное имя файла и возвращает относительный путь к сохраненному файлу.
     *
     * @param multipartFile загруженный файл изображения в формате {@link MultipartFile}
     * @return относительный путь к сохраненному файлу (префикс + имя файла)
     * @throws IOException если произошла ошибка при записи файла
     */
    String saveImage(MultipartFile multipartFile) throws IOException;

    /**
     * Читает содержимое файла изображения из файловой системы.
     *
     * @param filePath относительный путь к файлу изображения (включая префикс)
     * @return массив байтов с содержимым файла изображения
     * @throws IOException если произошла ошибка при чтении файла
     */
    byte[] getImage(String filePath) throws IOException;

    /**
     * Удаляет файл изображения из файловой системы, если он существует.
     *
     * @param filePath относительный путь к файлу изображения (включая префикс)
     * @throws IOException если произошла ошибка при удалении файла
     */
    void deleteImage(String filePath) throws IOException;
}