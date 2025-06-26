package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (IOException e) {
            log.error("Ошибка сохранения файла: {}", file.getOriginalFilename(), e);
            throw e;
        }
    }

    @Override
    public void deleteFile(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Ошибка удаления файла: {}", filename, e);
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.error("Ошибка чтения файла: {}", filename);
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            log.error("Ошибка чтения файла: {}", filename, e);
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }
}