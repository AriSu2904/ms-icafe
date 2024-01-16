package com.atm.inet.service.impl;

import com.atm.inet.entity.computer.*;
import com.atm.inet.entity.constant.ECategory;
import com.atm.inet.entity.constant.EStatus;
import com.atm.inet.model.request.ComputerRequest;
import com.atm.inet.model.request.ComputerSpecRequest;
import com.atm.inet.model.request.ComputerUpdateRequest;
import com.atm.inet.model.response.ComputerResponse;
import com.atm.inet.model.response.NewComputerResponse;
import com.atm.inet.repository.ComputerRepository;
import com.atm.inet.service.ComputerImageService;
import com.atm.inet.service.ComputerService;
import com.atm.inet.service.ComputerSpecService;
import com.atm.inet.service.TypeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ComputerServiceImplTest {

    @Mock
    private ComputerRepository computerRepository;

    @Mock
    private ComputerImageService computerImageService;

    @Mock
    private ComputerSpecService computerSpecService;

    @Mock
    private TypeService typeService;

    @Mock
    private ComputerService computerService;

    Computer computer;

    @BeforeEach
    void setUp() {
        computerService = new ComputerServiceImpl(computerRepository, computerImageService, computerSpecService, typeService);
        computer = Computer.builder()
                .id("1")
                .name("computer")
                .code("TEST01")
                .status(EStatus.FREE)
                .specification(new ComputerSpec())
                .type(new Type())
                .build();
    }

    @Test
    @DisplayName("Save computer by original entity")
    void testSaveComputer() {

        when(computerRepository.save(any(Computer.class))).thenReturn(computer);

        Computer saved = computerService.saveByComputer(computer);

        Assertions.assertEquals(saved.getId(), computer.getId());

    }

    @Test
    @DisplayName("Test save new computer")
    void testSave_newComputer() {
        ComputerRequest request = new ComputerRequest();
        request.setName("Computer1");
        request.setCode("123");
        request.setCategory("VIP");
        request.setPrice(2000L);
        request.setProcessor("Intel i7");
        request.setRam("16GB");
        request.setMonitor("24 inch");
        request.setSsd("512GB");
        request.setVga("NVIDIA RTX 3080");

        TypePrice typePrice = TypePrice.builder().price(request.getPrice()).isActive(true).build();
        Type type = new Type("1", ECategory.VIP, List.of(typePrice), new ComputerImage());

        MockMultipartFile multipartFile = new MockMultipartFile("test-file.jpg", "test-file.jpg", "image/jpeg", "test data".getBytes());
        ComputerImage computerImage = new ComputerImage();

        ComputerSpec computerSpec = ComputerSpec.builder()
                .processor(request.getProcessor())
                .ram(request.getRam())
                .monitor(request.getMonitor())
                .ssd(request.getSsd())
                .vga(request.getVga())
                .build();

        Computer computer = Computer.builder()
                .name(request.getName())
                .code(request.getCode())
                .status(EStatus.FREE)
                .specification(computerSpec)
                .type(type)
                .build();

        when(typeService.getOrSave(eq(ECategory.VIP), any())).thenReturn(type);
        when(computerImageService.create(eq(type), any(MultipartFile.class))).thenReturn(computerImage);
        when(computerSpecService.add(any(ComputerSpec.class))).thenReturn(computerSpec);
        when(computerRepository.saveAndFlush(any(Computer.class))).thenReturn(computer);

        NewComputerResponse response = computerService.save(request, multipartFile);

        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getCode(), response.getCode());
        assertEquals(EStatus.FREE.name(), response.getStatus());

    }

    @Test
    @DisplayName("test get computer by valid computer Id")
    void testGetComputer_validComputerId() {

        when(computerRepository.findById("1")).thenReturn(Optional.of(computer));

        Computer computerId = computerService.getByComputerId("1");

        Assertions.assertNotNull(computerId);
        Assertions.assertEquals(computerId.getId(), computer.getId());

    }

    @Test
    @DisplayName("test get computer by invalid computer Id")
    void testGetComputer_invalidComputerId() {

        when(computerRepository.findById("1")).thenThrow(ResponseStatusException.class);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Computer computerId = computerService.getByComputerId("1");

            Assertions.assertNull(computerId);
            Assertions.assertNotEquals(computerId.getId(), computer.getId());
        });
    }

    @Test
    @DisplayName("test get all computer")
    void testGetAllComputers() {

    }

    @Test
    @DisplayName("Test update computer with non-FREE status")
    void testUpdateComputer_nonFreeStatus() {
        ComputerUpdateRequest updateRequest = new ComputerUpdateRequest();
        updateRequest.setId("1");
        ComputerSpecRequest specRequest = new ComputerSpecRequest();
        updateRequest.setComputerSpec(specRequest);

        Computer computer = Computer.builder()
                .id("1")
                .status(EStatus.USED)
                .specification(new ComputerSpec())
                .build();

        when(computerRepository.findById("1")).thenReturn(java.util.Optional.of(computer));

        assertThrows(ResponseStatusException.class, () -> computerService.updateComputer(updateRequest));
    }

    @Test
    @DisplayName("Test delete computer by valid id")
    void testDeleteComputer_validId() {

        when(computerRepository.findById("1")).thenReturn(Optional.ofNullable(computer));

        Computer byComputerId = computerService.getByComputerId("1");

        Assertions.assertEquals(byComputerId.getId(), computer.getId());

    }

}