package com.atm.inet.service.impl;

import com.atm.inet.entity.BaseFile;
import com.atm.inet.service.BaseFileService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@Slf4j
public class BaseFileServiceImpl implements BaseFileService {

    @Value("${icafe.image-path-url}")
    private String path;

    @Override
    public BaseFile create(MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File can not be empty!");

        if (!List.of("image/jpeg", "image/png").contains(multipartFile.getContentType()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid Content Type!");

        try {
            Path directoryPath = Paths.get(path);
            Files.createDirectories(directoryPath);
            String filename = String.format("%d_%s", System.currentTimeMillis(), multipartFile.getOriginalFilename());
            Path filePath = directoryPath.resolve(filename);
            Files.copy(multipartFile.getInputStream(), filePath);

            return BaseFile.builder()
                    .name(filename)
                    .path(filePath.toString())
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .build();
        } catch (IOException | RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "a failure occurred on the server");
        }
    }

    @Override
    public Resource get(String path) {
        Path filePath = Paths.get(path);
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "a failure occurred on the server");
        }
    }
    @Override
    public void delete(String path) {
        try {
            Path filePath = Paths.get(path);
            log.info("Deleting file: {}", filePath);

            boolean deleted = Files.deleteIfExists(filePath);

            if (deleted) {
                log.info("File deleted: {}", filePath);
            } else {
                log.warn("File not found: {}", filePath);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
            }
        } catch (IOException e) {
            log.error("Failed to delete file: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file");
        } catch (RuntimeException e) {
            log.error("Internal error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }



}
