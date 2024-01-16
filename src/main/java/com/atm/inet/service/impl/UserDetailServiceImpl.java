package com.atm.inet.service.impl;

import com.atm.inet.entity.Role;
import com.atm.inet.entity.UserCredential;
import com.atm.inet.entity.UserDetailsImpl;
import com.atm.inet.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User With Email %s Not Found", email)));

        Role role = userCredential.getRole();
        Collection<SimpleGrantedAuthority> grantedAuthorities =
                Collections.singleton(new SimpleGrantedAuthority(role.getRole().name()));

        return UserDetailsImpl.builder()
                .email(userCredential.getEmail())
                .password(userCredential.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}
