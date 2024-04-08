package com.thogwa.thogwa.backend.service;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Override
    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        String fileName = file.getOriginalFilename();
        String ext = FilenameUtils.getExtension(fileName);
        String uniqueName = UUID.randomUUID() + "." + ext;

        try {
            byte[] bytes = file.getBytes();
            String filePath = "images/" + uniqueName;


            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filePath))) {
                stream.write(bytes);
                logger.info("File stored successfully: {}", filePath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error storing file: {}", ex.getMessage());
        }

        return uniqueName;
    }

    public FileSystemStorageService() {
        super();
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void delete(String filename) {

    }


//    public Path loadFile(String fileName) {
//        return Paths.get(uploadDir).resolve(fileName);
//    }

//    @Override
//    public Path load(String filename) {
//        return rootLocation.resolve(filename);
//    }


//    @Override
//    public Resource loadFile(String fileName) {
//        try {
//            Path file = Paths.get(uploadDir).resolve(fileName);
//            Resource resource = new UrlResource(file.toUri());
//
//            if (resource.exists() && resource.isReadable()) {
//                return resource;
//            } else {
//                throw new RuntimeException("Could not read file: " + fileName);
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Malformed URL for file: " + fileName, e);
//        }
//    }
//
//    public void deleteFile(String fileName) throws IOException {
//        Path filePath = Paths.get(uploadDir).resolve(fileName);
//        Files.deleteIfExists(filePath);
//    }


}
