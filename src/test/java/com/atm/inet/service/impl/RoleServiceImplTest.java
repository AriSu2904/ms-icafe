package com.atm.inet.service.impl;

import com.atm.inet.entity.Role;
import com.atm.inet.entity.constant.ERole;
import com.atm.inet.repository.RoleRepository;
import com.atm.inet.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleService roleService;

    Role role;
    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
        role = Role.builder().id("1").role(ERole.ROLE_ADMIN).build();
    }

    @Test
    @DisplayName("Get Role By Valid Role Name")
    void testGetRole_validRole() {
        when(roleRepository.findFirstByRole(ERole.ROLE_ADMIN)).thenReturn(Optional.of(role));

        Role savedRole = roleService.getOrSave(ERole.ROLE_ADMIN);

        assertEquals(savedRole.getRole(), role.getRole());
    }

    @Test
    @DisplayName("Save role")
    void testSaveRole(){

        Role roles = Role.builder()
                .id("2")
                .role(ERole.ROLE_ADMIN)
                .build();

        when(roleRepository.findFirstByRole(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());

        when(roleRepository.saveAndFlush(any(Role.class))).thenReturn(roles);

        Role savedRole = roleService.getOrSave(ERole.ROLE_ADMIN);

        assertEquals(savedRole.getRole(), role.getRole());

    }

}