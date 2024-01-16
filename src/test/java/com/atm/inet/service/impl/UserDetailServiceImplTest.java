package com.atm.inet.service.impl;

import com.atm.inet.entity.ProfilePicture;
import com.atm.inet.entity.Role;
import com.atm.inet.entity.UserCredential;
import com.atm.inet.entity.constant.ERole;
import com.atm.inet.repository.UserCredentialRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @Mock
    private UserDetailsService userDetailsService;

    UserCredential userCredential;
    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailServiceImpl(userCredentialRepository);

        userCredential = new UserCredential("1", "ari@gmail.com", "password", new Role("1", ERole.ROLE_ADMIN), true, new ProfilePicture());

    }

    @Test
    @DisplayName("Test Load by valid email")
    void testLoadByValidEmail() {
        String email = "ari@gmail.com";

        when(userCredentialRepository.findByEmail(email)).thenReturn(Optional.ofNullable(userCredential));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        Assertions.assertEquals(userDetails.getUsername(), email);

    }

    @Test
    @DisplayName("Test Load by invalid email")
    void testLoadByInvalidEmail() {
        String email = "ari@gmail.com";

        when(userCredentialRepository.findByEmail(email)).thenThrow(UsernameNotFoundException.class);

       Assertions.assertThrows(UsernameNotFoundException.class, () -> {
           UserDetails userDetails = userDetailsService.loadUserByUsername(email);

           Assertions.assertNull(userDetails);
           Assertions.assertNotEquals(userDetails.getUsername(), email);
       });

    }
}