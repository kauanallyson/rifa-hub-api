package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Vendedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    boolean existsByEmail(String email);

    boolean existsByTelefone(String telefone);

    Optional<Vendedor> findByEmail(String email);

    Optional<Vendedor> findByTelefone(String telefone);

    Optional<Vendedor> findByIdAndAtivoTrue(Long id);

    Page<Vendedor> findAllByNomeContainingIgnoreCaseAndAtivoTrue(String nome, Pageable pageable);

    Page<Vendedor> findAllByAtivoTrue(Pageable pageable);
}
