package com.kauanallyson.rifa_hub.repositories;

import com.kauanallyson.rifa_hub.entities.Premio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremioRepository extends JpaRepository<Premio, Long> {
}
