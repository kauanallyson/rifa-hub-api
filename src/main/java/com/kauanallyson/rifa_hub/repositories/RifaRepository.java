package com.kauanallyson.rifa_hub.repositories;

import com.kauanallyson.rifa_hub.entities.Rifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RifaRepository extends JpaRepository<Rifa, Long> {
}
