package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dtos.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.Ponto;
import org.springframework.stereotype.Component;

@Component
public class PontoMapper {

    public PontoResponseDTO toResponseDTO(Ponto ponto) {
        if (ponto == null) {
            return null;
        }
        String nomeComprador = ponto.getComprador() != null ? ponto.getComprador().getNome() : null;
        String nomeVendedor = ponto.getVendedor() != null ? ponto.getVendedor().getNome() : null;

        return new PontoResponseDTO(
                ponto.getNumero(),
                ponto.getStatus(),
                nomeComprador,
                nomeVendedor
        );
    }
}