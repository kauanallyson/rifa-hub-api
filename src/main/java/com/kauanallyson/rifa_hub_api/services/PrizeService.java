package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.prize.PrizeCreateDTO; // Usaremos um DTO de prêmio
import com.kauanallyson.rifa_hub_api.dtos.prize.PrizeResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.Prize;
import com.kauanallyson.rifa_hub_api.entities.Raffle;
import com.kauanallyson.rifa_hub_api.entities.enums.RaffleStatus;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.PrizeRepository;
import com.kauanallyson.rifa_hub_api.repositories.RaffleRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.PrizeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrizeService {

    private final PrizeRepository prizeRepository;
    private final RaffleRepository raffleRepository;
    private final PrizeMapper prizeMapper;

    @Transactional
    public PrizeResponseDTO addPrize(Long raffleId, PrizeCreateDTO dto) {
        Raffle raffle = raffleRepository.findById(raffleId)
                .orElseThrow(() -> new ResourceNotFoundException("Raffle with id " + raffleId + " not found"));

        if (raffle.getStatus() != RaffleStatus.OPEN) {
            throw new BusinessException("Only Raffles with status 'OPEN' can have prizes added");
        }

        boolean placingAlreadyExists = raffle.getPrizes().stream()
                .anyMatch(p -> p.getPlacement().equals(dto.placement()));
        if (placingAlreadyExists) {
            throw new BusinessException(dto.placement() + "° place already occupied");
        }

        Prize newPrize = prizeMapper.toEntity(dto, raffle);
        Prize savedPrize = prizeRepository.save(newPrize);

        return prizeMapper.toResponseDTO(savedPrize);
    }

    @Transactional
    public void deletePrize(Long raffleId, Long prizeId) {
        Raffle raffle = raffleRepository.findById(raffleId)
                .orElseThrow(() -> new ResourceNotFoundException("Raffle with id " + raffleId + " not found"));

        if (raffle.getStatus() != RaffleStatus.OPEN) {
            throw new BusinessException("Only Raffles with status 'OPEN' can have prizes removed");
        }

        Prize prize = prizeRepository.findById(prizeId)
                .orElseThrow(() -> new ResourceNotFoundException("Prize with id " + prizeId + " not found"));

        if (!prize.getRaffle().getId().equals(raffleId)) {
            throw new BusinessException("This prize doesn't belong to this raffle");
        }

        prizeRepository.delete(prize);
    }
}