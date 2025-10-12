package com.kauanallyson.rifa_hub_api.repositories;

import com.kauanallyson.rifa_hub_api.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByRaffleIdAndNumberIn(Long id, List<Integer> numbers);

    @Query("SELECT t FROM Ticket t WHERE t.raffle.id = :raffleId")
    List<Ticket> findAllByRaffleId(@Param("raffleId") Long raffleId);
}
