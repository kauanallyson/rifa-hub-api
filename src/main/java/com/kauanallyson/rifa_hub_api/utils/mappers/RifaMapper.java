package com.kauanallyson.rifa_hub_api.utils.mappers;

import com.kauanallyson.rifa_hub_api.dtos.rifa.RifaCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.rifa.RifaResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.rifa.RifaUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.Premio;
import com.kauanallyson.rifa_hub_api.entities.Rifa;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusPonto;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RifaMapper {

    private final PremioMapper premioMapper;

    public Rifa toEntity(RifaCreateDTO dto) {
        Rifa rifa = new Rifa();
        rifa.setNome(dto.nome());
        rifa.setDescricao(dto.descricao());
        rifa.setPontoPreco(dto.pontoPreco());
        rifa.setQuantidadePontos(dto.quantidadePontos());
        rifa.setDataSorteio(dto.dataSorteio());
        rifa.setStatus(StatusRifa.ABERTA);

        List<Premio> premios = dto.premios().stream()
                .map(premioDto -> premioMapper.toEntity(premioDto, rifa))
                .collect(Collectors.toList());
        rifa.setPremios(premios);

        return rifa;
    }

    public RifaResponseDTO toResponseDTO(Rifa rifa) {
        long pontosVendidos = rifa.getPontos().stream()
                .filter(p -> p.getStatus() == StatusPonto.VENDIDO)
                .count();

        return new RifaResponseDTO(
                rifa.getId(),
                rifa.getNome(),
                rifa.getDescricao(),
                rifa.getPremios().stream().map(premioMapper::toResponseDTO).collect(Collectors.toList()),
                rifa.getPontoPreco(),
                rifa.getDataSorteio(),
                rifa.getQuantidadePontos(),
                pontosVendidos,
                rifa.getQuantidadePontos() - pontosVendidos,
                rifa.getStatus()
        );
    }

    public void updateEntityFromDTO(RifaUpdateDTO dto, Rifa rifa) {
        rifa.setNome(dto.nome());
        rifa.setDescricao(dto.descricao());
        rifa.setPontoPreco(dto.pontoPreco());
        rifa.setDataSorteio(dto.dataSorteio());
    }
}