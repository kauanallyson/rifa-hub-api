package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Raffle;
import com.kauanallyson.rifa_hub_api.entities.enums.RaffleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RaffleRepository extends JpaRepository<Raffle, Long>, JpaSpecificationExecutor<Raffle> {
    boolean existsByName(String name);

    Optional<Raffle> findByName(String name);

    @Query("SELECT r FROM Raffle r WHERE r.status = :status")
    List<Raffle> findAllByStatus(@Param("status") RaffleStatus status);

    @Query("SELECT r FROM Raffle r WHERE lower(r.name) LIKE lower(concat('%', :name, '%')) AND r.status IN :statuses")
    List<Raffle> findAllByNameAndStatusIn(@Param("name") String name, @Param("statuses") Collection<RaffleStatus> statuses);


}