package com.atm.inet.service.impl;

import com.atm.inet.entity.computer.ComputerImage;
import com.atm.inet.entity.computer.Type;
import com.atm.inet.entity.computer.TypePrice;
import com.atm.inet.entity.constant.ECategory;
import com.atm.inet.model.response.TypeResponse;
import com.atm.inet.repository.TypeRepository;
import com.atm.inet.service.TypeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TypeServiceImplTest {


    @Mock
    private TypeRepository typeRepository;

    @Mock
    private ComputerImageServiceImpl computerService;

    @Mock
    private TypeService typeService;

    Type type;

    @BeforeEach
    void setUp() {
        typeService = new TypeServiceImpl(typeRepository, computerService);
        type = new Type("1", ECategory.VIP, List.of(TypePrice.builder().price(2000L).build()), new ComputerImage());
    }

    @Test
    @DisplayName("Find type by valid type id")
    void testFindType_validTypeId() {

        when(typeRepository.findById("1")).thenReturn(Optional.of(type));

        Type typeById = typeService.findTypeById("1");

        Assertions.assertNotNull(typeById);
        Assertions.assertEquals(type.getId(), typeById.getId());

    }

    @Test
    @DisplayName("Find type by invalid type id")
    void testFindType_invalidTypeId() {

        when(typeRepository.findById("1")).thenThrow(ResponseStatusException.class);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Type typeById = typeService.findTypeById("1");

            Assertions.assertNull(typeById);
            Assertions.assertNotEquals(type.getId(), typeById.getId());
        });


    }

    @Test
    @DisplayName("Test find by invalid type id and return TypeResponse")
    void testFindById_invalidId() {

        when(typeRepository.findById("1")).thenThrow(ResponseStatusException.class);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            TypeResponse typeById = typeService.findById("1");

            Assertions.assertNull(typeById);
            Assertions.assertNotEquals(type.getId(), typeById.getId());
        });


    }

    @Test
    void update() {

    }

    @Test
    @DisplayName("get or save type")
    void testGetOrSaveType() {

        when(typeRepository.saveAndFlush(type)).thenReturn(type);

        Type savedType = typeService.getOrSave(ECategory.EXTREME, new TypePrice());

        Assertions.assertEquals(type.getId(), savedType.getId());
    }

    @Test
    @DisplayName("Test get all types")
    void testGetAll() {
        Type type = new Type("1", ECategory.VIP, List.of(TypePrice.builder().price(2000L).isActive(true).build()), new ComputerImage());
        when(typeRepository.findAll()).thenReturn(List.of(type));

        List<TypeResponse> allTypes = typeService.getAll();

        assertNotNull(allTypes);
        assertEquals(1, allTypes.size());
        assertEquals(type.getId(), allTypes.get(0).getId());
    }


    @Test
    @DisplayName("Test download computer image")
    void testDownloadComputerImg() {
        Resource mockResource = mock(Resource.class);

        when(computerService.downloadImage("1")).thenReturn(mockResource);

        Resource downloadedResource = typeService.downloadComputerImg("1");

        assertNotNull(downloadedResource);
    }
}