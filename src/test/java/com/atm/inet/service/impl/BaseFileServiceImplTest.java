package com.atm.inet.service.impl;

import com.atm.inet.entity.BaseFile;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BaseFileServiceImplTest {

    @InjectMocks
    private BaseFileServiceImpl baseFileService;

    @Mock
    private Path directoryPath;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        baseFileService.setPath("src/test/resources/");
    }


    @Test
    @DisplayName("Test Create an empty file")
    void testCreate_emptyFile() {
        MockMultipartFile multipartFile = new MockMultipartFile("test-file.jpg", "test-file.jpg", "image/jpeg", new byte[0]);

        assertThrows(ResponseStatusException.class, () -> baseFileService.create(multipartFile));
    }

    @Test
    @DisplayName("Test Create with an invalid content type")
    void testCreate_invalidContentType() {
        MockMultipartFile multipartFile = new MockMultipartFile("test-file.txt", "test-file.txt", "text/plain", "test data".getBytes());

        assertThrows(ResponseStatusException.class, () -> baseFileService.create(multipartFile));
    }

    @Test
    @DisplayName("test get valid path file")
    void testGet_validFilePath() {
        String filePath = "/path/to/file.jpg";

        Resource resource = baseFileService.get(filePath);

        assertNotNull(resource);
    }

    @Test
    @DisplayName("Test get invalid Path file")
    void testGet_invalidFilePath() throws IOException {
        String filePath = "/non/existent/file.jpg";

        Resource resource = baseFileService.get(filePath);
        assertNotEquals(baseFileService.getPath(), resource.getFile().getPath());
    }

}
