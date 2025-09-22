package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dto.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub_api.dto.premio.PremioCreateDTO;
import com.kauanallyson.rifa_hub_api.dto.premio.PremioResponseDTO;
import com.kauanallyson.rifa_hub_api.dto.rifa.RifaCreateDTO;
import com.kauanallyson.rifa_hub_api.dto.rifa.RifaResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.Ponto;
import com.kauanallyson.rifa_hub_api.entities.Premio;
import com.kauanallyson.rifa_hub_api.entities.Rifa;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusPonto;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;
import com.kauanallyson.rifa_hub_api.repositories.RifaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RifaService {
    @Autowired
    RifaRepository repository;

    @Transactional
    public RifaResponseDTO createRifa(RifaCreateDTO dto){
        Rifa rifa = new Rifa();
        rifa.setNome(dto.nome());
        rifa.setDescricao(dto.descricao());
        rifa.setPontoPreco(dto.pontoPreco());
        rifa.setQuantidadePontos(dto.quantidadePontos());
        rifa.setDataSorteio(dto.dataSorteio());
        rifa.setStatus(StatusRifa.ABERTA);

        List<Premio> premios = dto.premios().stream()
                .map(premioDTO -> mapPremioDtoToEntity(premioDTO, rifa))
                .collect(Collectors.toList());
        rifa.setPremios(premios);

        List<Ponto> pontos = IntStream.rangeClosed(1, dto.quantidadePontos())
                .mapToObj(numero -> {
                    Ponto ponto = new Ponto();
                    ponto.setNumero(numero);
                    ponto.setStatus(StatusPonto.DISPONIVEL);
                    ponto.setRifa(rifa);
                    return ponto;
                })
                .toList();
        rifa.setPontos(pontos);
        Rifa rifaSalva = repository.saveAndFlush(rifa);
        return mapRifaToResponseDTO(rifaSalva);
    }

    private Premio mapPremioDtoToEntity(PremioCreateDTO dto, Rifa rifa){
        Premio premio = new Premio();
        premio.setDescricao(dto.descricao());
        premio.setColocacao(dto.colocacao());
        premio.setRifa(rifa);
        return premio;
    }

    private RifaResponseDTO mapRifaToResponseDTO(Rifa rifa) {
        Long pontosVendidos = rifa.getPontos().stream()
                .filter(p -> p.getStatus() == StatusPonto.VENDIDO)
                .count();
        Long pontosDisponiveis = rifa.getQuantidadePontos() - pontosVendidos;
        List<PremioResponseDTO> premiosDTO = rifa.getPremios().stream()
                .map(this::mapPremioToResponseDTO)
                .toList();
        return new RifaResponseDTO(
                rifa.getId(),
                rifa.getNome(),
                rifa.getDescricao(),
                premiosDTO,
                rifa.getPontoPreco(),
                rifa.getDataSorteio(),
                rifa.getQuantidadePontos(),
                pontosVendidos,
                pontosDisponiveis,
                rifa.getStatus()
        );
    }

    private PremioResponseDTO mapPremioToResponseDTO(Premio premio) {
        return new PremioResponseDTO(
                premio.getDescricao(),
                premio.getColocacao(),
                premio.getPontoVencedor() != null ? mapPontoToResponseDTO(premio.getPontoVencedor()) : null
        );
    }

    private PontoResponseDTO mapPontoToResponseDTO(Ponto ponto) {
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
