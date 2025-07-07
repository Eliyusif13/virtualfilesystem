package com.sadiqov.virtualfilesystem.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

@Service
public class FileSystemService {

    @Value("${virtualfs.root-path}")
    private String rootPath;

    public String createFolder(String relativePath) throws IOException {
        Path folderPath = Paths.get(rootPath, relativePath);
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
            return "Qovluq yaradıldı: " + folderPath;
        } else {
            return "Qovluq artıq mövcuddur: " + folderPath;
        }
    }

    public String deleteFolder(String relativePath) throws IOException {
        Path folderPath = Paths.get(rootPath, relativePath);
        if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
            Files.walk(folderPath)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Silinmə zamanı xəta: " + path, e);
                        }
                    });
            return "Qovluq və içindəkilər silindi: " + folderPath;
        } else {
            return "Qovluq tapılmadı.";
        }
    }

    public String createFile(String relativePath, String content) throws IOException {
        Path filePath = Paths.get(rootPath, relativePath);
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        Files.writeString(filePath, content + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        return "Fayl yaradıldı: " + filePath;
    }

    public String deleteFile(String relativePath) throws IOException {
        Path filePath = Paths.get(rootPath, relativePath);
        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            Files.delete(filePath);
            return "Fayl silindi: " + filePath;
        } else {
            return "Fayl tapılmadı və ya fayl deyil.";
        }
    }

    public String printTreeStructure() throws IOException {
        Path start = Paths.get(rootPath);
        StringBuilder builder = new StringBuilder();
        Files.walk(start)
                .forEach(path -> {
                    int depth = start.relativize(path).getNameCount();
                    String indent = " ".repeat(depth * 4);
                    builder.append(indent).append(path.getFileName()).append("\n");
                });

        return builder.toString();
    }
}