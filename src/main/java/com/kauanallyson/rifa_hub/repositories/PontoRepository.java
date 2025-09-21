package com.kauanallyson.rifa_hub.repositories;

import com.kauanallyson.rifa_hub.entities.Ponto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PontoRepository extends JpaRepository<Ponto, Long> {
}
