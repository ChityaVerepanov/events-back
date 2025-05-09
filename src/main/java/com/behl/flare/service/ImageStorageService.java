package com.behl.flare.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageStorageService {

    @Value("${application.files.storage}")
    private String uploadDir;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

//    private final String uploadDir = "_files_storage"; // относительный путь

    public String saveImage(MultipartFile file) throws IOException {
        // Проверка размера
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Файл слишком большой");
        }

        // Проверка расширения
        String originalName = file.getOriginalFilename();
        String ext = getExtension(originalName);
        if (ext == null || !ALLOWED_EXTENSIONS.contains(ext.toLowerCase())) {
            throw new IllegalArgumentException("Недопустимый формат файла");
        }

        // Генерация рандомного имени
        String randomName = UUID.randomUUID().toString() + "." + ext;

        // Создание каталога, если не существует
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Сохранение файла
        Path filePath = uploadPath.resolve(randomName);
        file.transferTo(filePath.toFile());

        return randomName;
    }

    private String getExtension(String filename) {
        if (filename == null) return null;
        int dot = filename.lastIndexOf('.');
        return (dot == -1) ? null : filename.substring(dot + 1);
    }


    public Resource loadImageAsResource(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Файл не найден: " + fileName);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при загрузке файла: " + fileName, ex);
        }
    }
}
