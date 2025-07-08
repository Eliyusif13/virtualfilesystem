package com.sadiqov.virtualfilesystem.controller;


import com.sadiqov.virtualfilesystem.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fs")
public class FileSystemController {

    @Autowired
    private FileSystemService fileSystemService;

    @PostMapping("/folder")
    public String createFolder(@RequestParam String path) throws IOException {
        return fileSystemService.createFolder(path);
    }

    @DeleteMapping("/folder")
    public String deleteFolder(@RequestParam String path) throws IOException {
        return fileSystemService.deleteFolder(path);
    }

    @PostMapping("/file")
    public String createFile(@RequestParam String path, @RequestBody String content) throws IOException {
        return fileSystemService.createFile(path, content);
    }

    @DeleteMapping("/file")
    public String deleteFile(@RequestParam String path) throws IOException {
        return fileSystemService.deleteFile(path);
    }

    @GetMapping("/tree")
    public String showTree() throws IOException {
        return fileSystemService.printTreeStructure();
    }

    @GetMapping("/search")
    public List<String> search(@RequestParam String name) throws IOException {
        List<Path> paths = fileSystemService.searchByName(name);
        return paths.stream()
                .map(Path::toString)
                .collect(Collectors.toList());

    }

    @PostMapping("/move")
    public String moveFile(@RequestParam String from, @RequestParam String to) throws IOException {
        return fileSystemService.copyOrRenameFile(from, to);
    }

    @GetMapping("/file/info")
    public String getFileInfo(@RequestParam String path) throws IOException {
        return fileSystemService.getFileInfo(path);
    }

}
