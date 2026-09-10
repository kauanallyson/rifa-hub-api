package com.kauanallyson.rifa_hub_api.raffle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RaffleRepository extends JpaRepository<Raffle, Long> {
    Optional<Raffle> findByName(String name);

    @Query(value = """
            SELECT * FROM raffles
            WHERE (CAST(:status AS text) IS NULL OR status = CAST(:status AS text))
            """, nativeQuery = true)
    List<Raffle> findByStatus(@Param("status") String status);

    @Query(value = """
            SELECT * FROM raffles
            WHERE (CAST(:status AS text) IS NULL OR status = CAST(:status AS text))
            AND to_tsvector('portuguese', coalesce(name, '') || ' ' || coalesce(description, ''))
                @@ websearch_to_tsquery('portuguese', :name)
            """, nativeQuery = true)
    List<Raffle> searchByStatus(@Param("name") String name, @Param("status") String status);
}
