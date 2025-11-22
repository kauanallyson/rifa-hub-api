package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.prize.PrizeCreate;
import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleCreate;
import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleResponse;
import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleUpdate;
import com.kauanallyson.rifa_hub_api.entities.Prize;
import com.kauanallyson.rifa_hub_api.entities.Raffle;
import com.kauanallyson.rifa_hub_api.entities.Ticket;
import com.kauanallyson.rifa_hub_api.enums.RaffleStatus;
import com.kauanallyson.rifa_hub_api.enums.TicketStatus;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.RaffleRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.RaffleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class RaffleService {
    private final RaffleRepository raffleRepository;
    private final RaffleMapper raffleMapper;

    // Create
    @Transactional
    public RaffleResponse createRaffle(RaffleCreate dto) {
        validateNameUniqueness(dto.name(), null);
        validatePrizePlacements(dto.prizes());

        Raffle raffle = raffleMapper.toEntity(dto);

        List<Ticket> tickets = generateTickets(dto.ticketAmount(), raffle);
        raffle.setTickets(tickets);

        Raffle savedRaffle = raffleRepository.save(raffle);
        return raffleMapper.toResponseDTO(savedRaffle);
    }

    // Find All
    @Transactional(readOnly = true)
    public List<RaffleResponse> findAll(String name, RaffleStatus status) {
        String filterName = (name != null && !name.isBlank()) ? name : null;

        List<Raffle> raffles = raffleRepository.findWithFilters(filterName, status);

        return raffles.stream()
                .map(raffleMapper::toResponseDTO)
                .toList();
    }

    // Find By Id
    @Transactional(readOnly = true)
    public RaffleResponse findRaffleById(Long id) {
        Raffle raffle = getRaffleOrThrow(id);
        return raffleMapper.toResponseDTO(raffle);
    }

    // Update
    @Transactional
    public RaffleResponse updateRaffle(RaffleUpdate dto, Long id) {
        Raffle raffle = getRaffleOrThrow(id);

        if (raffle.getStatus() != RaffleStatus.OPEN) {
            throw new BusinessException("Only Raffles with status 'OPEN' can be updated");
        }

        validateNameUniqueness(dto.name(), id);

        raffleMapper.updateEntityFromDTO(dto, raffle);
        raffleRepository.save(raffle);
        return raffleMapper.toResponseDTO(raffle);
    }

    // Delete
    @Transactional
    public void deleteRaffle(Long id) {
        Raffle raffle = getRaffleOrThrow(id);

        if (raffle.getStatus() == RaffleStatus.FINISHED) {
            throw new BusinessException("Cannot cancel a finished raffle");
        }
        if (raffle.getStatus() == RaffleStatus.CANCELED) {
            throw new BusinessException("Raffle already canceled");
        }

        boolean hasSoldTickets = raffle.getTickets().stream()
                .anyMatch(ticket -> ticket.getStatus() != TicketStatus.AVAILABLE);

        if (hasSoldTickets) {
            throw new BusinessException("Cannot cancel a raffle that has sold tickets");
        }

        raffle.setStatus(RaffleStatus.CANCELED);
        raffleRepository.save(raffle);
    }

    // Draw
    @Transactional
    public RaffleResponse drawWinningTickets(Long id) {
        Raffle raffle = getRaffleOrThrow(id);

        validateDrawPrerequisites(raffle);

        List<Ticket> ticketsSold = new ArrayList<>(raffle.getTickets().stream()
                .filter(t -> t.getStatus() == TicketStatus.SOLD)
                .toList());

        if (ticketsSold.size() < raffle.getPrizes().size()) {
            throw new BusinessException("Not enough tickets sold. Cannot perform draw");
        }

        Collections.shuffle(ticketsSold);

        int numberOfPrizes = raffle.getPrizes().size();
        for (int i = 0; i < numberOfPrizes; i++) {
            Prize prize = raffle.getPrizes().get(i);
            Ticket winningTicket = ticketsSold.get(i);
            prize.setWinningTicket(winningTicket);
        }

        raffle.setStatus(RaffleStatus.FINISHED);
        raffle.setDrawDate(LocalDateTime.now());

        Raffle finishedRaffle = raffleRepository.save(raffle);
        return raffleMapper.toResponseDTO(finishedRaffle);
    }

    // ---------------

    private Raffle getRaffleOrThrow(Long id) {
        return raffleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raffle with id " + id + " not found"));
    }

    private void validateNameUniqueness(String name, Long currentId) {
        Optional<Raffle> conflict = raffleRepository.findByName(name);
        if (conflict.isPresent()) {
            // Se for create (currentId == null) OU se for update e o ID for diferente
            if (currentId == null || !conflict.get().getId().equals(currentId)) {
                throw new DuplicateResourceException("Name already taken");
            }
        }
    }

    private void validatePrizePlacements(List<PrizeCreate> prizes) {
        List<Integer> placings = prizes.stream().map(PrizeCreate::placement).toList();
        Set<Integer> uniquePlacings = new HashSet<>(placings);
        if (uniquePlacings.size() < placings.size()) {
            throw new BusinessException("Only one prize per placement (1st, 2nd, 3rd...) allowed");
        }
    }

    private List<Ticket> generateTickets(long amount, Raffle raffle) {
        return LongStream.rangeClosed(1, amount)
                .mapToObj(number -> {
                    Ticket ticket = new Ticket();
                    ticket.setNumber(number);
                    ticket.setStatus(TicketStatus.AVAILABLE);
                    ticket.setRaffle(raffle);
                    return ticket;
                })
                .toList();
    }

    private void validateDrawPrerequisites(Raffle raffle) {
        if (raffle.getStatus() != RaffleStatus.OPEN) {
            throw new BusinessException("Only Raffles with status 'OPEN' can be drawn");
        }
        if (raffle.getDrawDate() == null) {
            throw new BusinessException("Draw date is invalid");
        }
    }
}