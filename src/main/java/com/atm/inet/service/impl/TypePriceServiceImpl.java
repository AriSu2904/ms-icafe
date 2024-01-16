package com.atm.inet.service.impl;

import com.atm.inet.entity.computer.TypePrice;
import com.atm.inet.repository.TypePriceRepository;
import com.atm.inet.service.TypePriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class TypePriceServiceImpl implements TypePriceService {

    private final TypePriceRepository typePriceRepository;

    @Override
    public TypePrice findByTypeId(String id) {
        return typePriceRepository.findByIsActiveTrueAndType_Id(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Price Not Found!"));    }


    @Override
    public TypePrice findByIsActive() {
        return typePriceRepository.findFirstByIsActiveTrue().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "is Active price not found!"));
    }

    @Override
    public TypePrice findById(String id) {
        return typePriceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Type price not found!"));
    }
}
