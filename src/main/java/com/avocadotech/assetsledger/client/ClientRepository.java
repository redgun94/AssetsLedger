package com.avocadotech.assetsledger.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    boolean existsByLoanOfficerIdAndEmail(UUID loanOfficerId, String email);

    List<Client> findAllByLoanOfficerId(UUID loanOfficerId);

    boolean existsById(UUID id);

    Optional<Client> findByIdAndLoanOfficerId(UUID id,UUID loanOfficerId);
}
