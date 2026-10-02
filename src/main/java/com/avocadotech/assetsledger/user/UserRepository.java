package com.avocadotech.assetsledger.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface  UserRepository extends JpaRepository<User, UUID>{

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByPhone(String phone);

    boolean existsByPhone(String phone);
}
