package com.atm.inet.service.impl;

import com.atm.inet.entity.Admin;
import com.atm.inet.entity.UserCredential;
import com.atm.inet.entity.UserDetailsImpl;
import com.atm.inet.model.request.UpdateAdminRequest;
import com.atm.inet.model.response.AdminResponse;
import com.atm.inet.repository.AdminRepository;
import com.atm.inet.repository.UserCredentialRepository;
import com.atm.inet.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserCredentialRepository userCredentialRepository;
    @Override
    public AdminResponse authenticateUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Admin admin = adminRepository.findFirstByUserCredential_Email(userDetails.getEmail()).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin Not Found!"));

        return generateResponse(admin);
    }
    @Override
    public Admin create(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public AdminResponse getById(String id) {
        return generateResponse(findById(id));
    }

    @Override
    public AdminResponse update(UpdateAdminRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AdminResponse adminResponse = authenticateUser(authentication);

        Admin admin = findById(request.getAdminId());

        if (!adminResponse.getAdminId().equals(admin.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource!");
        }

        Optional<Admin> optionalAdmin = adminRepository.findByPhoneNumber(request.getPhoneNumber());
        if(optionalAdmin.isPresent()){
            Admin existAdmin = optionalAdmin.get();
            if(existAdmin.getPhoneNumber().equals(request.getPhoneNumber()) && !existAdmin.getId().equals(request.getAdminId())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already in use!");
            }
        }


        admin.setFullName(request.getFullName());
        admin.setPhoneNumber(request.getPhoneNumber());
        adminRepository.save(admin);

        return generateResponse(admin);
    }


    @Override
    public Admin findById(String id) {
        return adminRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findFirstByUserCredential_Email(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    @Override
    public void delete(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AdminResponse adminResponse = authenticateUser(authentication);
        if (adminResponse.getAdminId().equals(id)) {
            Admin admin = adminRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Admin not found"));

            UserCredential userCredential = admin.getUserCredential();
            userCredential.setIsActive(false);
            userCredentialRepository.save(userCredential);

            adminRepository.delete(admin);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource!");

        }
    }

    private AdminResponse generateResponse(Admin admin) {
        return AdminResponse.builder()
                .adminId(admin.getId())
                .email(admin.getEmail())
                .fullName(admin.getFullName())
                .phoneNumber(admin.getPhoneNumber())
                .build();
    }


}
