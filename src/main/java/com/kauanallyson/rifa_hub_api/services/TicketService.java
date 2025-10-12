package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketOrderRequestDTO;
import com.kauanallyson.rifa_hub_api.dtos.ticket.TicketResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.Client;
import com.kauanallyson.rifa_hub_api.entities.Raffle;
import com.kauanallyson.rifa_hub_api.entities.Seller;
import com.kauanallyson.rifa_hub_api.entities.Ticket;
import com.kauanallyson.rifa_hub_api.entities.enums.TicketStatus;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.ClientRepository;
import com.kauanallyson.rifa_hub_api.repositories.RaffleRepository;
import com.kauanallyson.rifa_hub_api.repositories.SellerRepository;
import com.kauanallyson.rifa_hub_api.repositories.TicketRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final RaffleRepository raffleRepository;
    private final ClientRepository clientRepository;
    private final SellerRepository sellerRepository;
    private final TicketMapper ticketMapper;

    @Transactional
    public List<TicketResponseDTO> sellTickets(Long raffleId, TicketOrderRequestDTO dto) {
        Raffle raffle = raffleRepository.findById(raffleId)
                .orElseThrow(() -> new ResourceNotFoundException("Raffle with id " + raffleId + " not found"));

        Client client = clientRepository.findActiveById(dto.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + dto.clientId() + " not found"));

        Seller seller = sellerRepository.findActiveById(dto.sellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller with id " + dto.sellerId() + " not found"));

        List<Integer> requestedNumbers = dto.numbers();
        if (requestedNumbers == null || requestedNumbers.isEmpty()) {
            throw new BusinessException("At least one ticket number must be provided");
        }

        List<Ticket> ticketsToBuy = ticketRepository.findByRaffleIdAndNumberIn(raffleId, requestedNumbers);

        if (ticketsToBuy.size() != requestedNumbers.size()) {
            throw new ResourceNotFoundException("One or more of the requested ticket numbers do not exist for this raffle");
        }

        for (Ticket ticket : ticketsToBuy) {
            if (ticket.getStatus() != TicketStatus.AVAILABLE) {
                throw new BusinessException("Ticket number " + ticket.getNumber() + " is no longer available");
            }
        }

        LocalDateTime saleDate = LocalDateTime.now();
        for (Ticket ticket : ticketsToBuy) {
            ticket.setStatus(TicketStatus.SOLD);
            ticket.setClient(client);
            ticket.setSeller(seller);
            ticket.setSaleDate(saleDate);
        }
        List<Ticket> soldTickets = ticketRepository.saveAll(ticketsToBuy);
        return soldTickets.stream()
                .map(ticketMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDTO> getTicketsByRaffleId(Long raffleId) {
        if (!raffleRepository.existsById(raffleId)) {
            throw new ResourceNotFoundException("Raffle with id " + raffleId + " not found");
        }
        List<Ticket> tickets = ticketRepository.findAllByRaffleId(raffleId);
        return tickets.stream()
                .map(ticketMapper::toResponseDTO)
                .toList();
    }
}