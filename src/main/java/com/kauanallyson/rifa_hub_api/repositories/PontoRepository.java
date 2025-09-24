package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Ponto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PontoRepository extends JpaRepository<Ponto, Long> {
    Optional<Ponto> findByRifaIdAndNumero(Long rifaId, Integer numero);

    Page<Ponto> findAllByRifaId(Long rifaId, Pageable pageable);
}
