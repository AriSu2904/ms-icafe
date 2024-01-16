package com.atm.inet.service;

import com.atm.inet.entity.Role;
import com.atm.inet.entity.UserCredential;
import com.atm.inet.model.request.AuthRequest;
import com.atm.inet.model.response.LoginResponse;
import com.atm.inet.model.response.RegisterResponse;

public interface AuthService {

    RegisterResponse registerAdmin(AuthRequest request);

    RegisterResponse registerCustomer(AuthRequest request);

    LoginResponse login(AuthRequest request);

    UserCredential createUserCredential(AuthRequest request, Role role);


}
