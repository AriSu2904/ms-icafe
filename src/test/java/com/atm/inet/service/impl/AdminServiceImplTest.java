package com.atm.inet.service.impl;

import com.atm.inet.entity.Admin;
import com.atm.inet.entity.Role;
import com.atm.inet.entity.UserDetailsImpl;
import com.atm.inet.entity.constant.ERole;
import com.atm.inet.model.request.UpdateAdminRequest;
import com.atm.inet.model.response.AdminResponse;
import com.atm.inet.repository.AdminRepository;
import com.atm.inet.repository.UserCredentialRepository;
import com.atm.inet.service.AdminService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    AdminRepository adminRepository;
    UserCredentialRepository userCredentialRepository;
    AdminService adminService;

    //Entity
    Admin admin;
    UserDetails userDetails;

    @BeforeEach
    void setUp() {
        adminService = new AdminServiceImpl(adminRepository, userCredentialRepository);

        admin = new Admin("1", "ari@admin.com", "arisusanto", "012345678", null);
        userDetails = new UserDetailsImpl(admin.getId(), admin.getEmail(), "rahasia", Collections.singleton(new SimpleGrantedAuthority(ERole.ROLE_ADMIN.name())));
    }

    @Test
    @DisplayName("Authenticate Admin with valid email")
    void testAuthenticateAdmin_validEmail() {
        String userEmail = "ari@admin.com";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(adminRepository.findFirstByUserCredential_Email(userEmail)).thenReturn(Optional.of(admin));

        AdminResponse adminResponse = adminService.authenticateUser(authentication);


        Assertions.assertEquals(admin.getEmail(), adminResponse.getEmail());
        Assertions.assertNotNull(adminResponse);
    }

    @Test
    @DisplayName("Authenticate Admin with invalid email")
    void testAuthenticateAdmin_invalidEmail() {
        String userEmail = "ari@admin.com";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(adminRepository.findFirstByUserCredential_Email(userEmail)).thenThrow(ResponseStatusException.class);

        Assertions.assertThrows(ResponseStatusException.class,() -> {
            AdminResponse adminResponse = adminService.authenticateUser(authentication);
            Assertions.assertEquals(admin.getEmail(), adminResponse.getEmail());
            Assertions.assertNull(adminResponse);
        });
    }

    @Test
    @DisplayName("Create admin with valid data")
    void testCreateAdmin_validData() {

        Admin data = Admin.builder()
                .id("1")
                .email("ari@admin.com")
                .build();

        when(adminRepository.save(data)).thenReturn(data);

        Admin createdAdmin = adminService.create(data);

        Assertions.assertEquals(createdAdmin.getId(), admin.getId());
        Assertions.assertEquals(createdAdmin.getEmail(), admin.getEmail());

    }

    @Test
    @DisplayName("Find Admin By Valid Id")
    void testFindAdmin_validId(){
        String id = "1";

        when(adminRepository.findById(id)).thenReturn(Optional.of(admin));

        Admin data = adminService.findById(id);

        Assertions.assertEquals(data.getId(), admin.getId());
        Assertions.assertEquals(data.getFullName(), admin.getFullName());
    }


    @Test
    @DisplayName("Find Admin By invalid Id")
    void testFindAdmin_invalidId(){
        String id = "1";

        when(adminRepository.findById(id)).thenThrow(ResponseStatusException.class);
        Assertions.assertThrows(ResponseStatusException.class,() -> {
            Admin data = adminService.findById(id);
            Assertions.assertNotEquals(data.getId(), admin.getId());
            Assertions.assertNull(data);
        });
    }

    @Test
    @DisplayName("Find admin by valid user credential email")
    void testFindAdmin_validUserCredentialEmail(){
        String email = "ari@admin";

        when(adminRepository.findFirstByUserCredential_Email(email)).thenReturn(Optional.of(admin));

        Admin data = adminService.findByEmail(email);

        Assertions.assertEquals(data.getEmail(), admin.getEmail());
    }

    @Test
    @DisplayName("Find admin by invalid user credential email")
    void testFindAdmin_invalidUserCredentialEmail(){
        String email = "ari2@admin";

        when(adminRepository.findFirstByUserCredential_Email(email)).thenThrow(ResponseStatusException.class);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Admin data = adminService.findByEmail(email);
            Assertions.assertNull(data);
        });
    }

//    @Test
//    @DisplayName("Update Admin With Valid Data")
//    void testUpdate_ValidData() {
//        String adminId = "1";
//        UpdateAdminRequest updateRequest = new UpdateAdminRequest(adminId, "Updated Admin", "987654321");
//
//        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
//
//        adminService.update(updateRequest);
//
//        verify(adminRepository, times(1)).findById(adminId);
//        verify(adminRepository, times(1)).save(any(Admin.class));
//    }


}