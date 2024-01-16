package com.atm.inet.service.impl;

import com.atm.inet.entity.BaseFile;
import com.atm.inet.entity.computer.ComputerImage;
import com.atm.inet.entity.computer.Type;
import com.atm.inet.repository.ComputerImageRepository;
import com.atm.inet.service.BaseFileService;
import com.atm.inet.service.ComputerImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComputerImageServiceImpl implements ComputerImageService {

    private final BaseFileService baseFileService;
    private final ComputerImageRepository computerImageRepository;

    @Override
    public ComputerImage create(Type request, MultipartFile multipartFile) {
        BaseFile baseFile = baseFileService.create(multipartFile);
        return ComputerImage.builder()
                .name(baseFile.getName())
                .contentType(baseFile.getContentType())
                .path(baseFile.getPath())
                .size(baseFile.getSize())
                .type(request)
                .build();
    }

    @Override
    public ComputerImage findById(String id) {
        return computerImageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found!"));
    }

    @Override
    public Resource downloadImage(String id) {
        ComputerImage img = findById(id);
        return baseFileService.get(img.getPath());
    }

    @Override
    public String deleteById(String id) {
        ComputerImage image = findById(id);
        computerImageRepository.delete(image);
        return id;
    }
}
