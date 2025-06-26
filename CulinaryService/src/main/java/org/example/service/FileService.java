package org.example.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;

public interface FileService {
    String saveFile(MultipartFile file) throws IOException;
    void deleteFile(String filename);
    Resource load(String filename);
}

