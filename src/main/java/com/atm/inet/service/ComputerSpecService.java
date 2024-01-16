package com.atm.inet.service;

import com.atm.inet.entity.computer.Computer;
import com.atm.inet.entity.computer.ComputerSpec;

public interface ComputerSpecService {

    ComputerSpec findByComputerId(String id);
    ComputerSpec add(ComputerSpec spec);

}
