package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dto.premio.PremioCreateDTO;
import com.kauanallyson.rifa_hub_api.dto.premio.PremioResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.Premio;
import com.kauanallyson.rifa_hub_api.entities.Rifa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PremioMapper {

    private final PontoMapper pontoMapper;

    public PremioResponseDTO toResponseDTO(Premio premio) {
        return new PremioResponseDTO(
                premio.getDescricao(),
                premio.getColocacao(),
                pontoMapper.toResponseDTO(premio.getPontoVencedor())
        );
    }

    public Premio toEntity(PremioCreateDTO dto, Rifa rifa) {
        Premio premio = new Premio();
        premio.setDescricao(dto.descricao());
        premio.setColocacao(dto.colocacao());
        premio.setRifa(rifa);
        return premio;
    }
}