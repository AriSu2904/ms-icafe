package com.atm.inet.service;


import com.atm.inet.entity.computer.TypePrice;

public interface TypePriceService {

    TypePrice findByTypeId(String id);

    TypePrice findByIsActive();

    TypePrice findById(String id);


}
