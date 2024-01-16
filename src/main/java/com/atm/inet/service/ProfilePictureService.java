package com.atm.inet.service;

import com.atm.inet.entity.ProfilePicture;
import com.atm.inet.entity.UserCredential;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureService {
    ProfilePicture create(UserCredential userCredential, MultipartFile multipartFile);
    Resource download(String id);
    void deleteById(String id);


}
