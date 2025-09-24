package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Comprador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompradorRepository extends JpaRepository<Comprador, Long> {
    boolean existsByTelefone(String telefone);

    boolean existsByEmail(String email);

    Optional<Comprador> findByEmail(String email);

    Optional<Comprador> findByTelefone(String telefone);

    Optional<Comprador> findByIdAndAtivoTrue(Long id);

    Page<Comprador> findAllByAtivoTrue(Pageable pageable);

    Page<Comprador> findAllByNomeContainingIgnoreCaseAndAtivoTrue(String nome, Pageable pageable);
}
