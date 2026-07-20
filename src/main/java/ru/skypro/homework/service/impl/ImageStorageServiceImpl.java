package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ru.skypro.homework.service.ImageStorageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
// ВАЖНО: Убрали @RequiredArgsConstructor, так как есть свой конструктор
public class ImageStorageServiceImpl implements ImageStorageService {

    private final Path uploadDirectory;

    public ImageStorageServiceImpl(@Value("${app.upload-dir}") String uploadDirPath) {
        this.uploadDirectory = Paths.get(System.getProperty("user.dir"), uploadDirPath).normalize();

        if (!Files.exists(uploadDirectory)) {
            try {
                Files.createDirectories(uploadDirectory);
                System.out.println("Папка создана: " + uploadDirectory.toAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Не удалось создать папку для изображений!", e);
            }
        }
    }

    @Override
    public String saveImage(byte[] bytes, String originalFilename) {
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFileName = UUID.randomUUID() + fileExtension;
        Path filePath = uploadDirectory.resolve(uniqueFileName).normalize();

        if (!filePath.toAbsolutePath().startsWith(uploadDirectory.toAbsolutePath())) {
            throw new SecurityException("Недопустимое имя файла");
        }

        try {
            Files.write(filePath, bytes);
            return "/images/" + uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла", e);
        }
    }

    // ИСПРАВЛЕНИЕ: убрали throws MalformedURLException
    @Override
    public Resource getResource(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        Path filePath = uploadDirectory.resolve(filename).normalize();

        if (!filePath.toAbsolutePath().startsWith(uploadDirectory.toAbsolutePath())) {
            return null;
        }

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            // UrlResource конструктор может выбросить MalformedURLException,
            // но мы оборачиваем это в try-catch и возвращаем null или кидаем RuntimeException,
            // чтобы не менять сигнатуру интерфейса.
            try {
                return new UrlResource(filePath.toUri());
            } catch (MalformedURLException e) {
                // Логируем ошибку и возвращаем null, так как путь валидный, URI должен быть корректным
                return null;
            }
        } else {
            return null;
        }
    }
}
