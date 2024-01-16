package com.atm.inet.controller;

import com.atm.inet.model.common.CommonResponse;
import com.atm.inet.model.request.UpdateAdminRequest;
import com.atm.inet.model.response.AdminResponse;
import com.atm.inet.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponse<AdminResponse>> getById(@PathVariable String id){
        AdminResponse response= adminService.getById(id);
        return ResponseEntity.ok(CommonResponse.<AdminResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get admin data!")
                .data(response)
                .build());
    }

    @PutMapping
    public ResponseEntity<CommonResponse<AdminResponse>> update(@RequestBody UpdateAdminRequest request){
        AdminResponse updatedAdmin = adminService.update(request);
        return ResponseEntity.ok(CommonResponse.<AdminResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update data!")
                .data(updatedAdmin)
                .build());
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id){
        adminService.delete(id);
        CommonResponse<?> response = CommonResponse.builder()
                .data("Successfully deleting your admin account")
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
