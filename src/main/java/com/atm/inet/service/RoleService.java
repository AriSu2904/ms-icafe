package com.atm.inet.service;

import com.atm.inet.entity.Role;
import com.atm.inet.entity.constant.ERole;

public interface RoleService {

   Role getOrSave(ERole role);

}
