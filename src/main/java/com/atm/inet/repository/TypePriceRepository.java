package com.atm.inet.repository;

import com.atm.inet.entity.computer.Type;
import com.atm.inet.entity.computer.TypePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypePriceRepository extends JpaRepository<TypePrice, String> {
    Optional<TypePrice> findFirstByIsActiveTrue();

    Optional<TypePrice> findByIsActiveTrueAndType_Id(String typeId);
}
