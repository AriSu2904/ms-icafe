package com.atm.inet.repository;

import com.atm.inet.entity.computer.ComputerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputerImageRepository extends JpaRepository<ComputerImage, String> {
}
