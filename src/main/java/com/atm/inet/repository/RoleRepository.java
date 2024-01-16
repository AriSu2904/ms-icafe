package com.atm.inet.repository;

import com.atm.inet.entity.Role;
import com.atm.inet.entity.constant.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findFirstByRole(ERole role);
}
