package com.kauanallyson.rifa_hub.repositories;

import com.kauanallyson.rifa_hub.entities.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

    boolean existsByEmail(String email);

    boolean existsByTelefone(String phone);

    Optional<Vendedor> findByEmail(String email);
}