package com.atm.inet.controller;

import com.atm.inet.model.common.CommonResponse;
import com.atm.inet.model.request.UpdateAdminRequest;
import com.atm.inet.model.response.AdminResponse;
import com.atm.inet.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private AdminService adminService;

    private AdminController adminController;

    @BeforeEach
    void setUp() {
        adminService = mock(AdminService.class);
        adminController = new AdminController(adminService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("test admin controller, get admin by id")
    void testController_getAdminById() {
        String adminId = "123";
        AdminResponse adminResponse = new AdminResponse();
        when(adminService.getById(adminId)).thenReturn(adminResponse);

        ResponseEntity<CommonResponse<AdminResponse>> responseEntity = adminController.getById(adminId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(adminResponse, responseEntity.getBody().getData());
    }

    @Test
    @DisplayName("test admin controller, update admin")
    void testController_updateAdmin() {
        UpdateAdminRequest updateAdminRequest = new UpdateAdminRequest();
        AdminResponse updatedAdmin = new AdminResponse();
        when(adminService.update(updateAdminRequest)).thenReturn(updatedAdmin);

        ResponseEntity<CommonResponse<AdminResponse>> responseEntity = adminController.update(updateAdminRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(updatedAdmin, responseEntity.getBody().getData());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test controller delete admin")
    void testController_deleteAdmin() {
        String adminId = "123";
        doNothing().when(adminService).delete(adminId);

        ResponseEntity<?> responseEntity = adminController.delete(adminId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Successfully deleting your admin account", ((CommonResponse<?>) responseEntity.getBody()).getData());
    }

    @Test
    @DisplayName("test controller, should return access denied")
    void testDeleteAccessDenied() {
        String adminId = "123";
        doThrow(new AccessDeniedException("Access denied")).when(adminService).delete(adminId);

        assertThrows(AccessDeniedException.class, () -> {
            adminController.delete(adminId);
        });
    }
}
