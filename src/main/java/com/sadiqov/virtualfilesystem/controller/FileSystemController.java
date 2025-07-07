package com.sadiqov.virtualfilesystem.controller;


import com.sadiqov.virtualfilesystem.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/fs")
public class FileSystemController {

    @Autowired
    private FileSystemService fileSystemService;

    @PostMapping("/folder")
    public String createFolder(@RequestParam String path) throws IOException {
        return fileSystemService.createFolder(path);
    }

    @PostMapping("/file")
    public String createFile(@RequestParam String path, @RequestBody String content) throws IOException {
        return fileSystemService.createFile(path, content);
    }

    @DeleteMapping("/file")
    public String deleteFile(@RequestParam String path) throws IOException {
        return fileSystemService.deleteFile(path);
    }

    @DeleteMapping("/folder")
    public String deleteFolder(@RequestParam String path) throws IOException {
        return fileSystemService.deleteFolder(path);
    }

}
