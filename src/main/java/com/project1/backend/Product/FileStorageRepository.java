package com.project1.backend.Product;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface FileStorageRepository {
    String storeFile(MultipartFile file) throws IOException;

    void deleteFile(String fileName) throws IOException;
}
