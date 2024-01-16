package com.atm.inet.controller;

import com.atm.inet.model.common.CommonResponse;
import com.atm.inet.model.common.ComputerSearch;
import com.atm.inet.model.common.PagingResponse;
import com.atm.inet.model.request.ComputerRequest;
import com.atm.inet.model.request.ComputerUpdateRequest;
import com.atm.inet.model.response.ComputerResponse;
import com.atm.inet.model.response.NewComputerResponse;
import com.atm.inet.service.ComputerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/computers")
@CrossOrigin("*")
public class ComputerController {

    private final ObjectMapper mapper;

    private final ComputerService computerService;

    @PostMapping(
            headers = ("Content-Type=multipart/form-data"),
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<NewComputerResponse>> addComputer(
            @RequestPart(name = "computer") String request,
            @RequestPart MultipartFile image
            ){
        log.warn("WARNING FROM CONTROLLER, {},", request);
        log.warn("WARNING FROM CONTROLLER, {},", image);

        ComputerRequest computerRequest;
        try {
            computerRequest = mapper.readValue(request, ComputerRequest.class);

            NewComputerResponse savedComputer = computerService.save(computerRequest, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    CommonResponse.<NewComputerResponse>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Successfully add data!")
                            .data(savedComputer)
                            .build()
            );

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Input!");
        }
        }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "50", required = false) Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String processor,
            @RequestParam(required = false) String vga,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status
    ) {
        ComputerSearch dataSearch = ComputerSearch.builder()
                .name(name)
                .code(code)
                .processor(processor)
                .vga(vga)
                .category(category)
                .status(status)
                .build();

        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<ComputerResponse> pageableResponse = computerService.getAll(pageRequest, dataSearch);

        PagingResponse pagingResponse = PagingResponse.builder()
                .count(pageableResponse.getTotalElements())
                .totalPages(pageableResponse.getTotalPages())
                .page(pageNumber)
                .size(size)
                .build();

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully Get Data Per Page!")
                        .data(pageableResponse.getContent())
                        .pagingResponse(pagingResponse)
                        .build()
        );
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<ComputerResponse>> getById(@PathVariable String id) {
        ComputerResponse computer = computerService.getById(id);
        return ResponseEntity.ok(
                CommonResponse.<ComputerResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get data!")
                        .data(computer)
                        .build()
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<String>> deleteById(@PathVariable String id){
        String deletedEntity = computerService.deleteById(id);
        return ResponseEntity.ok(
                CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete data!")
                        .data(deletedEntity)
                        .build()
        );
    }

    @PutMapping
    public ResponseEntity<CommonResponse<ComputerResponse>> updateComputer(@RequestBody ComputerUpdateRequest update) {

        ComputerResponse computerResponse = computerService.updateComputer(update);
        return ResponseEntity.status(HttpStatus.OK).body(
                CommonResponse.<ComputerResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully update Computer")
                        .data(computerResponse)
                        .build()
        );
    }
//
//    @GetMapping(path = "/image/{imageId}")
//    public ResponseEntity<?> downloadImg(@PathVariable(name = "imageId") String id){
//        Resource resource = computerService.downloadComputerImg(id);
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.IMAGE_PNG)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
//    }

}
