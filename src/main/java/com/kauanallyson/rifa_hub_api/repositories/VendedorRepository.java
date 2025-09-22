package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    boolean existsByEmail(String email);
    Optional<String> findByNome(String nome);
}
