package com.atm.inet.service;

import com.atm.inet.entity.BaseFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface BaseFileService {

    BaseFile create(MultipartFile multipartFile);
    Resource get(String path);
    void delete(String path);


}
