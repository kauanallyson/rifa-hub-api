package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dtos.comprador.CompradorCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.comprador.CompradorResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.comprador.CompradorUpdateDTO;
import com.kauanallyson.rifa_hub_api.dtos.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.vendedor.VendedorUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.Comprador;
import com.kauanallyson.rifa_hub_api.entities.Vendedor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompradorMapper {

    private final PontoMapper pontoMapper;

    public Comprador toEntity(CompradorCreateDTO dto) {
        Comprador comprador = new Comprador();
        comprador.setNome(dto.nome());
        comprador.setTelefone(dto.telefone());
        comprador.setEmail(dto.email());
        return comprador;
    }

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
    public void updateEntityFromDTO(CompradorUpdateDTO dto, Comprador comprador) {
        comprador.setNome(dto.nome());
        comprador.setTelefone(dto.telefone());
        comprador.setEmail(dto.email());
    }
}