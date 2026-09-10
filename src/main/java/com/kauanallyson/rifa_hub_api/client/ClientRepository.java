package com.kauanallyson.rifa_hub_api.client;

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

    @Query(value = "SELECT * FROM clients WHERE active = true", nativeQuery = true)
    List<Client> findAllActive();

    @Query(value = """
            SELECT * FROM clients
            WHERE active = true
            AND to_tsvector('simple', name) @@ websearch_to_tsquery('simple', :name)
            """, nativeQuery = true)
    List<Client> searchActiveByName(@Param("name") String name);
}
