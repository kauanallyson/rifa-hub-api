package com.kauanallyson.rifa_hub_api.ticket;

import com.kauanallyson.rifa_hub_api.client.Client;
import com.kauanallyson.rifa_hub_api.client.ClientRepository;
import com.kauanallyson.rifa_hub_api.raffle.Raffle;
import com.kauanallyson.rifa_hub_api.raffle.RaffleRepository;
import com.kauanallyson.rifa_hub_api.seller.Seller;
import com.kauanallyson.rifa_hub_api.seller.SellerRepository;
import com.kauanallyson.rifa_hub_api.shared.enums.RaffleStatus;
import com.kauanallyson.rifa_hub_api.shared.enums.TicketStatus;
import com.kauanallyson.rifa_hub_api.shared.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.shared.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketOrderRequest;
import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final RaffleRepository raffleRepository;
    private final ClientRepository clientRepository;
    private final SellerRepository sellerRepository;
    private final TicketMapper ticketMapper;

    // --- Sell Tickets ---
    @Transactional
    public List<TicketResponse> sellTickets(Long raffleId, TicketOrderRequest dto) {
        Raffle raffle = getRaffleOrThrow(raffleId);
        if (raffle.getStatus() != RaffleStatus.OPEN) {
            throw new BusinessException("Tickets can only be sold for OPEN raffles");
        }

        if (dto.numbers() == null || dto.numbers().isEmpty()) {
            throw new BusinessException("At least one ticket number must be provided");
        }

        Client client = getClientOrThrow(dto.clientId());
        Seller seller = getSellerOrThrow(dto.sellerId());

        List<Ticket> ticketsToBuy = ticketRepository.findByRaffleIdAndNumberIn(raffleId, dto.numbers());

        validateTicketAvailability(ticketsToBuy, dto.numbers().size());

        LocalDateTime now = LocalDateTime.now();
        ticketsToBuy.forEach(ticket -> {
            ticket.setStatus(TicketStatus.SOLD);
            ticket.setClient(client);
            ticket.setSeller(seller);
            ticket.setSaleDate(now);
        });

        List<Ticket> soldTickets = ticketRepository.saveAll(ticketsToBuy);

        return soldTickets.stream()
                .map(ticketMapper::toResponseDTO)
                .toList();
    }

    // Get tickets
    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsByRaffleId(Long raffleId, TicketStatus status) {
        if (!raffleRepository.existsById(raffleId)) {
            throw new ResourceNotFoundException("Raffle with id " + raffleId + " not found");
        }

        List<Ticket> tickets = ticketRepository.findByRaffleIdAndStatus(raffleId, status);

        return tickets.stream()
                .map(ticketMapper::toResponseDTO)
                .toList();
    }

    // ---------------
    private void validateTicketAvailability(List<Ticket> ticketsFound, int requestedAmount) {
        if (ticketsFound.size() != requestedAmount) {
            throw new ResourceNotFoundException("One or more requested ticket numbers do not exist for this raffle");
        }

        List<Long> unavailableNumbers = ticketsFound.stream()
                .filter(t -> t.getStatus() != TicketStatus.AVAILABLE)
                .map(Ticket::getNumber)
                .toList();

        if (!unavailableNumbers.isEmpty()) {
            throw new BusinessException("The following ticket numbers are not available: " + unavailableNumbers);
        }
    }

    private Raffle getRaffleOrThrow(Long id) {
        return raffleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raffle with id " + id + " not found"));
    }

    private Client getClientOrThrow(Long id) {
        return clientRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));
    }

    private Seller getSellerOrThrow(Long id) {
        return sellerRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with id " + id + " not found"));
    }
}