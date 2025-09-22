package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Rifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RifaRepository extends JpaRepository<Rifa, Long> {
    boolean existsByNome(String nome);
    Optional<Rifa> findByNome(String nome);
}
