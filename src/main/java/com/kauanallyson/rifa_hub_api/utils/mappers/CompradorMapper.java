package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dto.comprador.CompradorCreateDTO;
import com.kauanallyson.rifa_hub_api.dto.comprador.CompradorResponseDTO;
import com.kauanallyson.rifa_hub_api.dto.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.Comprador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompradorMapper {

    private final PontoMapper pontoMapper;

    public CompradorResponseDTO toResponseDTO(Comprador comprador) {
        List<PontoResponseDTO> pontosDto = comprador.getPontosComprados() != null ?
                comprador.getPontosComprados().stream()
                        .map(pontoMapper::toResponseDTO)
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        return new CompradorResponseDTO(
                comprador.getId(),
                comprador.getNome(),
                comprador.getTelefone(),
                comprador.getEmail(),
                pontosDto
        );
    }

    public Comprador toEntity(CompradorCreateDTO dto) {
        Comprador comprador = new Comprador();
        comprador.setNome(dto.nome());
        comprador.setTelefone(dto.telefone());
        comprador.setEmail(dto.email());
        return comprador;
    }
}