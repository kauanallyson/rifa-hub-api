package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.prize.PrizeCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.raffle.RaffleUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.Prize;
import com.kauanallyson.rifa_hub_api.entities.Ticket;
import com.kauanallyson.rifa_hub_api.entities.Raffle;
import com.kauanallyson.rifa_hub_api.entities.enums.TicketStatus;
import com.kauanallyson.rifa_hub_api.entities.enums.RaffleStatus;
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
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RaffleService {
    private final RaffleRepository raffleRepository;
    private final RaffleMapper raffleMapper;

    // Create
    @Transactional
    public RaffleResponseDTO createRaffle(RaffleCreateDTO dto) {
        if (raffleRepository.existsByName(dto.name())) {
            throw new DuplicateResourceException("Name already taken");
        }
        List<Integer> placings = dto.prizes().stream().map(PrizeCreateDTO::placing).toList();
        if (new HashSet<>(placings).size() < placings.size()) {
            throw new BusinessException("Only one prize per placing");
        }
        Raffle raffle = raffleMapper.toEntity(dto);

        List<Ticket> tickets = IntStream.rangeClosed(1, dto.ticketAmount())
            .mapToObj(numero -> {
            Ticket ticket = new Ticket();
            ticket.setNumber(numero);
            ticket.setStatus(TicketStatus.AVAILABLE);
            ticket.setRaffle(raffle);
            return ticket;
            })
            .toList();
        raffle.setTickets(tickets);
        Raffle savedRaffle = raffleRepository.save(raffle);
        return raffleMapper.toResponseDTO(savedRaffle);
    }

    // Get all Active
    @Transactional(readOnly = true)
    public List<RaffleResponseDTO> getAllActiveRaffles() {
        List<Raffle> raffles = raffleRepository.findAllByStatus(RaffleStatus.OPEN);
        return raffles.stream()
                .map(raffleMapper::toResponseDTO)
                .toList();
    }

    // Get all by Status
    @Transactional(readOnly = true)
    public List<RaffleResponseDTO> getAllRaffleByStatus(RaffleStatus status) {
        List<Raffle> raffles = raffleRepository.findAllByStatus(status);
        return raffles.stream()
                .map(raffleMapper::toResponseDTO)
                .toList();
    }

    // Find by Name
    @Transactional(readOnly = true)
    public List<RaffleResponseDTO> findRaffleByName(String name) {
        List<RaffleStatus> desiredStatus = new ArrayList<>();
        desiredStatus.add(RaffleStatus.OPEN);
        List<Raffle> raffles = raffleRepository.findAllByNameAndStatusIn(name, desiredStatus);
        return raffles.stream()
                .map(raffleMapper::toResponseDTO)
                .toList();
    }

    // Find by id
    @Transactional(readOnly = true)
    public RaffleResponseDTO findRaffleById(Long id) {
        Raffle raffle = raffleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raffle with id " + id + " not found"));
        return raffleMapper.toResponseDTO(raffle);
    }

    // Update
    @Transactional
    public RaffleResponseDTO updateRaffle(RaffleUpdateDTO dto, Long id) {
        Raffle raffle = raffleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raffle with id " + id + " not found"));

        if (raffle.getStatus() != RaffleStatus.OPEN) {
            throw new BusinessException("Only Raffles with status 'OPEN' can be updated");
        }

        Optional<Raffle> raffleWithSameName = raffleRepository.findByName(dto.name());
        if (raffleWithSameName.isPresent() && !raffleWithSameName.get().getId().equals(id)) {
            throw new DuplicateResourceException("Name already taken");
        }

        raffleMapper.updateEntityFromDTO(dto, raffle);
        raffleRepository.save(raffle);
        return raffleMapper.toResponseDTO(raffle);
    }

    // Delete
    @Transactional
    public void deleteRaffle(Long id) {
        Raffle raffle = raffleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raffle with id " + id + " not found"));

        if (raffle.getStatus() == RaffleStatus.FINISHED) {
            throw new BusinessException("Only Raffles with status 'FINISHED' can be updated");
        }
        if (raffle.getStatus() == RaffleStatus.CANCELED) {
            throw new BusinessException("Raffle already canceled");
        }
        boolean hasSoldTickets = raffle.getTickets()
                .stream()
                .anyMatch(ponto -> ponto.getStatus() != TicketStatus.AVAILABLE);
        if (hasSoldTickets) {
            throw new BusinessException("Cannot cancel a raffle that has sold points");
        }

        raffle.setStatus(RaffleStatus.CANCELED);
        raffleRepository.save(raffle);
    }

    @Transactional
    public RaffleResponseDTO drawWinningTickets(Long id) {
        Raffle raffle = raffleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raffle with id " + id + " not found"));

        if (raffle.getStatus() != RaffleStatus.OPEN) {
            throw new BusinessException("Only Raffles with status 'OPEN' can be drawn");
        }

        if (raffle.getDrawDate() != null && LocalDateTime.now().isBefore(raffle.getDrawDate())) {
            throw new BusinessException("Draw date has not been reached yet");
        }

        List<Ticket> ticketsSold = new java.util.ArrayList<>(raffle.getTickets().stream()
                .filter(ponto -> ponto.getStatus() == TicketStatus.SOLD)
                .toList());

        if (ticketsSold.isEmpty()) {
            throw new BusinessException("No tickets sold. Cannot perform draw");
        }

        int numberOfPrizes = raffle.getPrizes().size();
        if (ticketsSold.size() < numberOfPrizes) {
            throw new BusinessException("Not enough tickets sold. Cannot perform draw");
        }

        Collections.shuffle(ticketsSold);
        List<Ticket> drawnTickets = ticketsSold.subList(0, numberOfPrizes);

        for (int i = 0; i < numberOfPrizes; i++) {
            Prize prize = raffle.getPrizes().get(i);
            Ticket winningTicket = drawnTickets.get(i);
            prize.setWinningTicket(winningTicket);
        }

        raffle.setStatus(RaffleStatus.FINISHED);
        Raffle finishedRaffle = raffleRepository.save(raffle);
        return raffleMapper.toResponseDTO(finishedRaffle);
    }
}