package com.atm.inet.controller;

import com.atm.inet.model.common.CommonResponse;
import com.atm.inet.model.response.FileResponse;
import com.atm.inet.model.response.UserResponse;
import com.atm.inet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/me")
    public ResponseEntity<CommonResponse<UserResponse>> getUserInfo(Authentication authentication) {
        UserResponse userInfo = userService.getUserInfo(authentication);
        return ResponseEntity.ok(
                CommonResponse.<UserResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully Get User Info")
                        .data(userInfo)
                        .build()
        );
    }

    @PutMapping(path = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<FileResponse>> uploadProfilePicture(@RequestParam(name = "image") MultipartFile multipartFile) {
        FileResponse fileResponse = userService.updateProfilePicture(multipartFile);
        System.out.println("MULTIPART USER CONTROLLER" + multipartFile);
        System.out.println("INFO DARI USER CONTROLLER" + fileResponse);
        return ResponseEntity.ok(
                CommonResponse.<FileResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully Upload Profile Picture!")
                        .data(fileResponse)
                        .build()
        );
    }

    @DeleteMapping(path = "/profile-picture/{imageId}")
    public ResponseEntity<?> deleteProfilePicture(@PathVariable(name = "imageId") String imageId) {
        userService.deleteProfilePicture(imageId);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully delete image")
                .data("OK")
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "profile-picture/{imageId}")
    public ResponseEntity<?> downloadPicture(@PathVariable(name = "imageId") String id){
        Resource resource = userService.downloadProfilePicture(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
