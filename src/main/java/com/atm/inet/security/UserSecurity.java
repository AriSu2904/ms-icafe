package com.atm.inet.security;

import com.atm.inet.entity.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserSecurity {

    public void validateUserByEmail(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        if (!principal.getEmail().equals(email))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource!");
    }

}
