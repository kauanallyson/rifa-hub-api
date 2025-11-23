package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<Client> findByEmail(String email);

    Optional<Client> findByPhone(String phone);

    @Query("SELECT c FROM Client c WHERE c.id = :id AND c.active = true")
    Optional<Client> findActiveById(@Param("id") Long id);

    @Query("""
            SELECT c FROM Client c
            WHERE (:name IS NULL OR (LOWER(c.name) LIKE :name))
            AND c.active = true
            """)
    List<Client> findAllActiveByName(@Param("name") String name);
}
