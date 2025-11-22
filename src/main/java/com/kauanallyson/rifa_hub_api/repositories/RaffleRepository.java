package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Raffle;
import com.kauanallyson.rifa_hub_api.enums.RaffleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RaffleRepository extends JpaRepository<Raffle, Long>, JpaSpecificationExecutor<Raffle> {
    boolean existsByName(String name);

    Optional<Raffle> findByName(String name);

    @Query("""
            SELECT r FROM Raffle r
            WHERE (:status IS NULL OR r.status = :status)
            AND (:name IS NULL OR lower(r.name) LIKE lower(concat('%', :name, '%')))
            """)
    List<Raffle> findWithFilters(@Param("name") String name, @Param("status") RaffleStatus status);
}