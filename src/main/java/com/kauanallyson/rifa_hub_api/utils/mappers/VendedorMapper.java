package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dto.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub_api.dto.vendedor.VendedorCreateDTO;
import com.kauanallyson.rifa_hub_api.dto.vendedor.VendedorResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.Vendedor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VendedorMapper {

    private final PontoMapper pontoMapper;

    public VendedorResponseDTO toResponseDTO(Vendedor vendedor) {
        List<PontoResponseDTO> pontosDto = vendedor.getPontosVendidos() != null ?
                vendedor.getPontosVendidos().stream()
                        .map(pontoMapper::toResponseDTO)
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        return new VendedorResponseDTO(
                vendedor.getId(),
                vendedor.getNome(),
                vendedor.getTelefone(),
                vendedor.getEmail(),
                pontosDto
        );
    }

    public Vendedor toEntity(VendedorCreateDTO dto) {
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(dto.nome());
        vendedor.setTelefone(dto.telefone());
        vendedor.setEmail(dto.email());
        return vendedor;
    }
}