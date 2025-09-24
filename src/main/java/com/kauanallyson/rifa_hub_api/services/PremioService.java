package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.premio.PremioCreateDTO; // Usaremos um DTO de prêmio
import com.kauanallyson.rifa_hub_api.dtos.premio.PremioResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.Premio;
import com.kauanallyson.rifa_hub_api.entities.Rifa;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.PremioRepository;
import com.kauanallyson.rifa_hub_api.repositories.RifaRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.PremioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PremioService {

    private final PremioRepository premioRepository;
    private final RifaRepository rifaRepository;
    private final PremioMapper premioMapper;

    @Transactional
    public PremioResponseDTO adicionarPremio(Long rifaId, PremioCreateDTO dto) {
        Rifa rifa = rifaRepository.findById(rifaId)
                .orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + rifaId + " não encontrada."));

        if (rifa.getStatus() != StatusRifa.ABERTA) {
            throw new BusinessException("Apenas rifas com status 'ABERTA' podem ter prêmios adicionados.");
        }

        boolean colocacaoJaExiste = rifa.getPremios().stream()
                .anyMatch(p -> p.getColocacao().equals(dto.colocacao()));
        if (colocacaoJaExiste) {
            throw new BusinessException("A colocação " + dto.colocacao() + " já existe para esta rifa.");
        }

        Premio novoPremio = premioMapper.toEntity(dto, rifa);
        Premio premioSalvo = premioRepository.save(novoPremio);

        return premioMapper.toResponseDTO(premioSalvo);
    }

    @Transactional
    public void removerPremio(Long rifaId, Long premioId) {
        Rifa rifa = rifaRepository.findById(rifaId)
                .orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + rifaId + " não encontrada."));

        if (rifa.getStatus() != StatusRifa.ABERTA) {
            throw new BusinessException("Apenas rifas com status 'ABERTA' podem ter prêmios removidos.");
        }

        Premio premio = premioRepository.findById(premioId)
                .orElseThrow(() -> new ResourceNotFoundException("Prêmio com id " + premioId + " não encontrado."));

        if (!premio.getRifa().getId().equals(rifaId)) {
            throw new BusinessException("Este prêmio não pertence à rifa informada.");
        }

        premioRepository.delete(premio);
    }
}