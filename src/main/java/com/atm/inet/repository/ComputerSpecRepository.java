package com.atm.inet.repository;

import com.atm.inet.entity.computer.ComputerSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComputerSpecRepository extends JpaRepository<ComputerSpec, String> {


}
