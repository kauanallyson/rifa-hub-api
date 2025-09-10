package com.kauanallyson.rifa_hub.repositories;

import com.kauanallyson.rifa_hub.entities.Ponto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PontoRepository extends JpaRepository<Ponto, Long> {
    List<Ponto> findByRifaId(Long rifaId);

    List<Ponto> findByVendaId(Long vendaId);

    boolean existsByRifaIdAndNumero(Long rifaId, Integer numero);
}
