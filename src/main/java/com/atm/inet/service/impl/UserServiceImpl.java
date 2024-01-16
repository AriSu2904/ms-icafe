package com.atm.inet.service.impl;

import com.atm.inet.entity.*;
import com.atm.inet.entity.constant.ERole;
import com.atm.inet.model.response.AdminResponse;
import com.atm.inet.model.response.CustomerResponse;
import com.atm.inet.model.response.FileResponse;
import com.atm.inet.model.response.UserResponse;
import com.atm.inet.repository.UserCredentialRepository;
import com.atm.inet.service.AdminService;
import com.atm.inet.service.CustomerService;
import com.atm.inet.service.ProfilePictureService;
import com.atm.inet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserCredentialRepository userCredentialRepository;
    private final ProfilePictureService profilePictureService;
    private final AdminService adminService;
    private final CustomerService customerService;

    @Override
    public UserResponse getUserInfo(Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        UserCredential userCredential = userCredentialRepository.findByEmail(principal.getEmail()).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Credentials"));

        if(userCredential.getRole().getRole().equals(ERole.ROLE_CUSTOMER)){
            Customer customer = customerService.findByEmail(userCredential.getEmail());
            log.warn("PROFILE PICTURE : {}", userCredential.getProfilePicture());
            return UserResponse.builder()
                    .userId(userCredential.getId())
                    .id(customer.getId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .phoneNumber(customer.getPhoneNumber())
                    .isMember(customer.getIsMember())
                    .email(userCredential.getEmail())
                    .role(userCredential.getRole().getRole().name())
                    .fileResponse(Objects.nonNull(userCredential.getProfilePicture()) ?
                            FileResponse.builder()
                                    .id(userCredential.getProfilePicture().getId())
                                    .filename(userCredential.getProfilePicture().getName())
                                    .url("/api/v1/users/profile-picture/" + userCredential.getProfilePicture().getId())
                                    .build(): null)
                    .build();

        } else if(userCredential.getRole().getRole().equals(ERole.ROLE_ADMIN)){
            Admin admin = adminService.findByEmail(userCredential.getEmail());
            log.warn("PROFILE PICTURE : {}", userCredential.getProfilePicture());
            return UserResponse.builder()
                    .userId(userCredential.getId())
                    .id(admin.getId())
                    .fullName(admin.getFullName())
                    .email(userCredential.getEmail())
                    .phoneNumber(admin.getPhoneNumber())
                    .role(userCredential.getRole().getRole().name())
                    .fileResponse(Objects.nonNull(userCredential.getProfilePicture()) ?
                            FileResponse.builder()
                                    .id(userCredential.getProfilePicture().getId())
                                    .filename(userCredential.getProfilePicture().getName())
                                    .url("/api/v1/users/profile-picture/" + userCredential.getProfilePicture().getId())
                                    .build(): null)
                    .build();
        } else {
            throw new IllegalStateException("Unexpected role encountered");
        }
    }


    @Override
    public UserCredential getByAuthentication(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userCredentialRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Credentials!"));

    }

    @Override
    public Resource downloadProfilePicture(String imageId) {
        return profilePictureService.download(imageId);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public FileResponse updateProfilePicture(MultipartFile multipartFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserCredential userCredential = getByAuthentication(authentication);
        ProfilePicture profilePicture = profilePictureService.create(userCredential, multipartFile);
        return FileResponse.builder()
                .id(profilePicture.getId())
                .filename(profilePicture.getName())
                .url("/api/v1/users/profile-picture/" + profilePicture.getId())
                .build();

    }
    @Override
    public void deleteProfilePicture(String imageId) {
        profilePictureService.deleteById(imageId);

    }

}
