package com.atm.inet.service.impl;

import com.atm.inet.entity.computer.Type;
import com.atm.inet.entity.computer.TypePrice;
import com.atm.inet.model.response.TypePriceResponse;
import com.atm.inet.repository.TypePriceRepository;
import com.atm.inet.service.TypePriceService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TypePriceServiceImplTest {

    @Mock
    private TypePriceRepository typePriceRepository;

    @Mock
    private TypePriceService typePriceService;


    TypePrice typePrice;
    @BeforeEach
    void setUp() {
        typePriceService = new TypePriceServiceImpl(typePriceRepository);
        typePrice = new TypePrice("1", new Type(), 100L, true);
    }

    @Test
    @DisplayName("Find price by valid type id")
    void testFindPriceBy_validTypeId() {

        when(typePriceRepository.findByIsActiveTrueAndType_Id("1")).thenReturn(Optional.of(typePrice));

        TypePrice price = typePriceService.findByTypeId("1");

        Assertions.assertNotNull(price);
        Assertions.assertEquals(price.getId(), typePrice.getId());
    }

    @Test
    @DisplayName("Find price by invalid type id")
    void testFindPriceBy_invalidTypeId() {

        when(typePriceRepository.findByIsActiveTrueAndType_Id("1")).thenThrow(ResponseStatusException.class);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            TypePrice price = typePriceService.findByTypeId("1");

            Assertions.assertNull(price);
            Assertions.assertNotEquals(price.getId(), typePrice.getId());
        });
    }


    @Test
    @DisplayName("Find price by valid price id")
    void testFindTypePrice_validId() {

        when(typePriceRepository.findById("1")).thenReturn(Optional.ofNullable(typePrice));

        TypePrice typePriceServiceById = typePriceService.findById("1");

        Assertions.assertNotNull(typePriceServiceById);
        Assertions.assertEquals(typePriceServiceById.getId(), typePrice.getId());

    }

}