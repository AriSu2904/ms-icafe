package com.atm.inet.service.impl;

import com.atm.inet.entity.Role;
import com.atm.inet.entity.constant.ERole;
import com.atm.inet.repository.RoleRepository;
import com.atm.inet.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(ERole role) {
        return roleRepository.findFirstByRole(role).orElseGet(()
                -> roleRepository.saveAndFlush(Role.builder().role(role).build()));
    }
}
