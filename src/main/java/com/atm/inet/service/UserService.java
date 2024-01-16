package com.atm.inet.service;


import com.atm.inet.entity.UserCredential;
import com.atm.inet.model.response.FileResponse;
import com.atm.inet.model.response.UserResponse;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserResponse getUserInfo(Authentication authentication);
    UserCredential getByAuthentication(Authentication authentication);
    FileResponse updateProfilePicture(MultipartFile multipartFile);

    Resource downloadProfilePicture(String imageId);
    void deleteProfilePicture(String imageId);
}
