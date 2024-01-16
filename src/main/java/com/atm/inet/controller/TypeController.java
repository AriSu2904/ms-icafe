package com.atm.inet.controller;

import com.atm.inet.entity.computer.Type;
import com.atm.inet.model.common.CommonResponse;
import com.atm.inet.model.request.TypeRequest;
import com.atm.inet.model.response.TypeResponse;
import com.atm.inet.service.TypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/types")
public class TypeController {

    private final TypeService typeService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<TypeResponse>>> findAll() {
        List<TypeResponse> getAll = typeService.getAll();
        return ResponseEntity.ok(
                CommonResponse.<List<TypeResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all data")
                        .data(getAll)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TypeResponse>> findById(@PathVariable(name = "id") String id) {
        TypeResponse getAll = typeService.findById(id);
        return ResponseEntity.ok(
                CommonResponse.<TypeResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all data")
                        .data(getAll)
                        .build()
        );
    }

    @PutMapping
    public ResponseEntity<?> update(
            @RequestBody TypeRequest request,
            @RequestParam(name = "image", required = false) MultipartFile multipartFiles
    ) {
            TypeResponse updatedType = typeService.update(request);
            return ResponseEntity.ok(
                    CommonResponse.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully update type")
                            .data(updatedType)
                            .build()
            );

    }


    @GetMapping(path = "/image/{imageId}")
    public ResponseEntity<?> downloadImg(@PathVariable(name = "imageId") String id) {
        Resource resource = typeService.downloadComputerImg(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

}
