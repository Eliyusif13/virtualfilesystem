package com.sadiqov.virtualfilesystem.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public List<Path> searchByName(String name) throws IOException {
        Path start = Paths.get(rootPath);
        List<Path> result = new ArrayList<>();

        Files.walk(start)
                .filter(path -> path.getFileName().toString().contains(name))
                .forEach(result::add);

        return result;
    }

    public String copyOrRenameFile(String from, String to) throws IOException {
        Path sourcePath = Paths.get(rootPath, from);
        Path targetPath = Paths.get(rootPath, to);

        if (Files.exists(sourcePath) && Files.isRegularFile(sourcePath)) {
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "Fayl köçürüldü və ya adı dəyişdi: " + sourcePath + " -> " + targetPath;
        } else {
            return "Fayl tapılmadı.";
        }
    }

    public String getFileInfo(String relativePath) throws IOException {
        Path filePath = Paths.get(rootPath, relativePath);

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            long fileSize = Files.size(filePath);

            FileTime fileTime = Files.getLastModifiedTime(filePath);

            return "Fayl ölçüsü: " + fileSize + " byte\n" +
                    "Yaradılma tarixi: " + fileTime.toString();
        } else {
            return "Fayl tapılmadı və ya fayl deyil.";
        }
    }

}