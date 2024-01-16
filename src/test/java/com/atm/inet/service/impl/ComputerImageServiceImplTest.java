package com.atm.inet.service.impl;

import com.atm.inet.entity.BaseFile;
import com.atm.inet.entity.computer.ComputerImage;
import com.atm.inet.entity.computer.Type;
import com.atm.inet.service.BaseFileService;
import com.atm.inet.repository.ComputerImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ComputerImageServiceImplTest {

    @Mock
    private BaseFileService baseFileService;
    @Mock
    private ComputerImageRepository computerImageRepository;

    private ComputerImageServiceImpl computerImageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        computerImageService = new ComputerImageServiceImpl(baseFileService, computerImageRepository);
    }

    @Test
    @DisplayName("Create valid multipart")
    void testCreate() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("test-image.jpg", new byte[0]);

        when(baseFileService.create(any(MultipartFile.class))).thenReturn(
                new BaseFile(null,"test-image.jpg", "image/jpeg", "/path/to/image.jpg",1000L)
        );

        ComputerImage expectedImage = ComputerImage.builder()
                .name("test-image.jpg")
                .contentType("image/jpeg")
                .path("/path/to/image.jpg")
                .size(1000L)
                .type(Type.builder().build())
                .build();

        ComputerImage actualImage = computerImageService.create(Type.builder().build(),multipartFile);

        assertEquals(expectedImage, actualImage);

    }

    @Test
    @DisplayName("test find by exist file id")
    void testFindById_validFileId() {
        String imageId = "image-id";

        when(computerImageRepository.findById(imageId)).thenReturn(
                Optional.of(new ComputerImage())
        );

        ComputerImage image = computerImageService.findById(imageId);

        assertNotNull(image);
    }

    @Test
    @DisplayName("test find by invalid file id")
    void testFindById_invalidFileId() {
        String nonExistingImageId = "non-existing-id";

        when(computerImageRepository.findById(nonExistingImageId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> computerImageService.findById(nonExistingImageId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Image not found!", exception.getReason());

    }

    @Test
    @DisplayName("test download image by valid id")
    void testDownloadImage_validId() {
        String imageId = "image-id";

        ComputerImage computerImage = new ComputerImage();
        computerImage.setPath("/path/to/image.jpg");

        when(computerImageRepository.findById(imageId)).thenReturn(Optional.of(computerImage));

        ByteArrayResource byteArrayResource = new ByteArrayResource(new byte[0]);
        when(baseFileService.get(computerImage.getPath())).thenReturn(byteArrayResource);

        Resource resource = computerImageService.downloadImage(imageId);

        assertNotNull(resource);
        assertEquals(byteArrayResource, resource);

    }

    @Test
    @DisplayName("test download image with invalidId")
    void testDownloadImage_invalidId() {
        String nonExistingImageId = "non-existing-id";

        when(computerImageRepository.findById(nonExistingImageId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> computerImageService.downloadImage(nonExistingImageId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Image not found!", exception.getReason());

    }

    @Test
    @DisplayName("Test delete image by valid image id")
    void testDeleteImg_validImgId() {
        String imageId = "image-id";

        when(computerImageRepository.findById(imageId)).thenReturn(
                Optional.of(new ComputerImage())
        );

        String deletedId = computerImageService.deleteById(imageId);

        assertEquals(imageId, deletedId);
    }

    @Test
    @DisplayName("Test delete image by invalid image id")
    void testDeleteImg_invalidImgId() {
        String nonExistingImageId = "non-existing-id";

        when(computerImageRepository.findById(nonExistingImageId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> computerImageService.deleteById(nonExistingImageId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Image not found!", exception.getReason());

    }
}
