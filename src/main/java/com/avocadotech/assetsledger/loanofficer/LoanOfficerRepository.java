package com.avocadotech.assetsledger.loanofficer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface LoanOfficerRepository extends JpaRepository<LoanOfficer, UUID> {
    Optional<LoanOfficer> findByNmls(String nmls);

    boolean existsByNmls(String nmls);


}
