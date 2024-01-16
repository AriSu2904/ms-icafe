package com.atm.inet.service.impl;

import com.atm.inet.entity.computer.*;
import com.atm.inet.entity.constant.ECategory;
import com.atm.inet.entity.constant.EStatus;
import com.atm.inet.model.common.ComputerSearch;
import com.atm.inet.model.request.ComputerRequest;
import com.atm.inet.model.request.ComputerUpdateRequest;
import com.atm.inet.model.response.*;
import com.atm.inet.repository.ComputerRepository;
import com.atm.inet.service.ComputerImageService;
import com.atm.inet.service.ComputerService;
import com.atm.inet.service.ComputerSpecService;
import com.atm.inet.service.TypeService;
import com.atm.inet.utils.specification.ComputerSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComputerServiceImpl implements ComputerService {

    private final ComputerRepository computerRepository;
    private final ComputerImageService computerImageService;
    private final ComputerSpecService computerSpecService;
    private final TypeService typeService;


    @Override
    public Computer saveByComputer(Computer computer) {
        return computerRepository.save(computer);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NewComputerResponse save(ComputerRequest request, MultipartFile multipartFiles) {

        TypePrice typePrice = TypePrice.builder()
                .price(request.getPrice())
                .isActive(true)
                .build();

        Type type = typeService.getOrSave(ECategory.valueOf(request.getCategory().toUpperCase()), typePrice);

        typePrice.setType(type);


        ComputerImage computerImage = computerImageService.create(type, multipartFiles);

        type.setComputerImage(computerImage);

        ComputerSpec computerSpec = ComputerSpec.builder()
                .processor(request.getProcessor())
                .ram(request.getRam())
                .monitor(request.getMonitor())
                .ssd(request.getSsd())
                .vga(request.getVga())
                .build();

        computerSpecService.add(computerSpec);

        Computer computer = Computer.builder()
                .name(request.getName())
                .code(request.getCode())
                .status(EStatus.FREE)
                .specification(computerSpec)
                .type(type)
                .build();

        computerRepository.saveAndFlush(computer);

        return generateNewComputerResponse(computer, type, computerSpec);
    }

    @Override
    public Computer getByComputerId(String id) {
        return computerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Computer not found!"));

    }

    @Override
    public Page<ComputerResponse> getAll(Pageable pageable, ComputerSearch computerSearch) {
        Specification<Computer> computerSpecification = ComputerSpecification.getSpecification(computerSearch);
        Page<Computer> computers = computerRepository.findAll(computerSpecification, pageable);
        log.warn("COMPUTERS FROM REPOSITORY : {}", computers);
        return computers.map(this::generateComputerResponse);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ComputerResponse updateComputer(ComputerUpdateRequest updateComputer) {
        Computer computer = getComputerById(updateComputer.getId());

        if(!computer.getStatus().equals(EStatus.FREE)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you can't update this PC right now!");

        ComputerSpec spec = ComputerSpec.builder()
                .id(computer.getSpecification().getId())
                .processor(updateComputer.getComputerSpec().getProcessor())
                .ram(updateComputer.getComputerSpec().getRam())
                .monitor(updateComputer.getComputerSpec().getMonitor())
                .ssd(updateComputer.getComputerSpec().getSsd())
                .vga(updateComputer.getComputerSpec().getVga())
                .build();

        computer.setName(updateComputer.getName());
        computer.setCode(updateComputer.getCode());
        computer.setSpecification(spec);

        computerRepository.save(computer);

        return generateComputerResponse(computer);
    }

    @Override
    public ComputerResponse getById(String id) {
        Computer computer = computerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Computer not found!"));
        return generateComputerResponse(computer);
    }

    @Override
    public String deleteById(String id) {
        Computer computer = getComputerById(id);
        if(computer.getCode() != null){
            computer.setCode(null);
            computer.setStatus(EStatus.DELETED);
            computerRepository.save(computer);
            return id;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found!");
    }

    private Computer getComputerById(String id) {
        return computerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Computer not found!"));
    }
    public ComputerResponse generateComputerResponse(Computer computer) {
        List<TypePriceResponse> priceResponses = computer.getType().getTypePrices().stream()
                .map(typePrice -> TypePriceResponse.builder()
                        .id(typePrice.getId())
                        .price(typePrice.getPrice())
                        .isActive(typePrice.getIsActive())
                        .build())
                .toList();

        TypeResponse typeResponse = TypeResponse.builder()
                .id(computer.getType().getId())
                .category(computer.getType().getCategory().name())
                .prices(priceResponses)
                .image(generateFileResponse(computer.getType()))
                .build();


        ComputerSpecResponse specResponse = ComputerSpecResponse.builder()
                .id(computer.getSpecification().getId())
                .processor(computer.getSpecification().getProcessor())
                .ram(computer.getSpecification().getRam())
                .monitor(computer.getSpecification().getMonitor())
                .ssd(computer.getSpecification().getSsd())
                .vga(computer.getSpecification().getVga())
                .build();


        return ComputerResponse.builder()
                .id(computer.getId())
                .name(computer.getName())
                .code(computer.getCode())
                .type(typeResponse)
                .status(computer.getStatus().name())
                .specification(specResponse)
                .build();
    }

    private FileResponse generateFileResponse(Type type) {
        return FileResponse.builder()
                        .id(type.getComputerImage().getId())
                        .filename(type.getComputerImage().getName())
                        .url("/api/v1/types/image/" + type.getComputerImage().getId())
                        .build();
    }
    private NewComputerResponse generateNewComputerResponse(Computer computer, Type type, ComputerSpec computerSpec) {

        List<TypePriceResponse> priceResponses = new ArrayList<>();

        type.getTypePrices().forEach(typePrice ->
                priceResponses.add(TypePriceResponse.builder()
                        .id(typePrice.getId())
                        .price(typePrice.getPrice())
                        .isActive(typePrice.getIsActive())
                        .build())
        );

        TypeResponse typeResponse = TypeResponse.builder()
                .id(type.getId())
                .category(type.getCategory().name())
                .prices(priceResponses)
                .build();

        ComputerSpecResponse specResponse = ComputerSpecResponse.builder()
                .id(computerSpec.getId())
                .processor(computerSpec.getProcessor())
                .ram(computerSpec.getRam())
                .monitor(computerSpec.getMonitor())
                .ssd(computerSpec.getSsd())
                .vga(computerSpec.getVga())
                .build();

        return NewComputerResponse.builder()
                .id(computer.getId())
                .name(computer.getName())
                .code(computer.getCode())
                .status(computer.getStatus().name())
                .category(typeResponse)
                .computerSpec(specResponse)
                .build();
    }
}
