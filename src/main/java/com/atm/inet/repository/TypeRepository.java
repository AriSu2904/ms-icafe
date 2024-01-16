package com.atm.inet.repository;

import com.atm.inet.entity.computer.Type;
import com.atm.inet.entity.constant.ECategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, String> {
    Optional<Type> findByCategory(ECategory category);

}
