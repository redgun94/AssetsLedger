package com.avocadotech.assetsledger.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByCompanyNmls(String nmls);
    Optional<Company> findByCompanyNmls(String nmls);
    List<Company> findByNameContainingIgnoreCase(String name);
}
