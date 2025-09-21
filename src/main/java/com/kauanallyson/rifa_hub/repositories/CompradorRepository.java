package com.kauanallyson.rifa_hub.repositories;

import com.kauanallyson.rifa_hub.entities.Comprador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompradorRepository extends JpaRepository<Comprador, Long> {
}
