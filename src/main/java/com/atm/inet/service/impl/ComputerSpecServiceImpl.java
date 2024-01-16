package com.atm.inet.service.impl;

import com.atm.inet.entity.computer.ComputerSpec;
import com.atm.inet.repository.ComputerRepository;
import com.atm.inet.repository.ComputerSpecRepository;
import com.atm.inet.service.ComputerSpecService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComputerSpecServiceImpl implements ComputerSpecService {

    private final ComputerSpecRepository computerSpecRepository;

    @Override
    public ComputerSpec add(ComputerSpec spec) {
        return computerSpecRepository.save(spec);
    }

    @Override
    public ComputerSpec findByComputerId(String id) {
        return null;
    }
}
