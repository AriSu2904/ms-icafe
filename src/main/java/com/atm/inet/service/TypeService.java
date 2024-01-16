package com.atm.inet.service;

import com.atm.inet.entity.computer.Type;
import com.atm.inet.entity.computer.TypePrice;
import com.atm.inet.entity.constant.ECategory;
import com.atm.inet.model.request.TypeRequest;
import com.atm.inet.model.response.TypeResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TypeService {


    List<TypeResponse> getAll();

    TypeResponse findById(String id);

    TypeResponse update(TypeRequest request);

    Type findTypeById(String id);

    Type getOrSave(ECategory category, TypePrice price);

    Resource downloadComputerImg(String id);
}
