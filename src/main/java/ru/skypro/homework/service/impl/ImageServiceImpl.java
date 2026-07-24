package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Реализация сервиса для работы с изображениями.
 * Обеспечивает сохранение, чтение и удаление файлов изображений в файловой системе.
 * Имена файлов генерируются с использованием UUID для предотвращения конфликтов.
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    /**
     * Базовый путь к директории для хранения изображений — корневая папка проекта.
     */
    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir");

    /**
     * Префикс пути, возвращаемый клиенту при сохранении изображения.
     */
    private static final String IMAGES = "/images/";

    /**
     * Путь к поддиректории для хранения изображений, задаваемый через конфигурацию приложения.
     */
    @Value("${path.dir.image}")
    private String imagePath;

    /**
     * Сохраняет загруженный файл изображения в файловую систему.
     * Создает необходимую директорию, если она не существует, генерирует уникальное имя файла
     * и перемещает файл из временного хранилища в целевую директорию.
     *
     * @param multipartFile загруженный файл изображения в формате {@link MultipartFile}
     * @return относительный путь к сохраненному файлу (префикс + имя файла)
     * @throws IOException если произошла ошибка при записи файла
     */
    @Override
    public String saveImage(MultipartFile multipartFile) throws IOException {
        File directory = new File(IMAGE_DIRECTORY + imagePath);
        log.info("Папка с изображениями находится по пути: {}", directory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        File dest = new File(directory, fileName);
        log.info("Файл будет создан по этому пути: {}", dest);

        multipartFile.transferTo(dest);

        return IMAGES + fileName;
    }

    /**
     * Читает содержимое файла изображения из файловой системы.
     * Возвращает массив байтов, представляющий содержимое файла.
     *
     * @param filePath относительный путь к файлу изображения (включая префикс)
     * @return массив байтов с содержимым файла изображения
     * @throws IOException если произошла ошибка при чтении файла
     */
    @Override
    public byte[] getImage(String filePath) throws IOException {
        File file = new File(IMAGE_DIRECTORY + imagePath + filePath.replace(IMAGES, ""));

        return Files.readAllBytes(file.toPath());
    }

    /**
     * Удаляет файл изображения из файловой системы, если он существует.
     *
     * @param filePath относительный путь к файлу изображения (включая префикс)
     * @throws IOException если произошла ошибка при удалении файла
     */
    @Override
    public void deleteImage(String filePath) throws IOException {
        Path path = Path.of(IMAGE_DIRECTORY + imagePath + filePath.replace(IMAGES, ""));

        Files.deleteIfExists(path);
    }
}
