package com.kauanallyson.rifa_hub_api.prize;

import com.kauanallyson.rifa_hub_api.prize.dtos.PrizeCreate;
import com.kauanallyson.rifa_hub_api.prize.dtos.PrizeResponse;
import com.kauanallyson.rifa_hub_api.raffle.Raffle;
import com.kauanallyson.rifa_hub_api.raffle.RaffleRepository;
import com.kauanallyson.rifa_hub_api.shared.enums.RaffleStatus;
import com.kauanallyson.rifa_hub_api.shared.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.shared.exceptions.ResourceNotFoundException;
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
    public PrizeResponse addPrize(Long raffleId, PrizeCreate dto) {
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