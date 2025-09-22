package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dto.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub_api.dto.premio.PremioCreateDTO;
import com.kauanallyson.rifa_hub_api.dto.premio.PremioResponseDTO;
import com.kauanallyson.rifa_hub_api.dto.rifa.RifaCreateDTO;
import com.kauanallyson.rifa_hub_api.dto.rifa.RifaResponseDTO;
import com.kauanallyson.rifa_hub_api.dto.rifa.RifaUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.Ponto;
import com.kauanallyson.rifa_hub_api.entities.Premio;
import com.kauanallyson.rifa_hub_api.entities.Rifa;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusPonto;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.RifaRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.PremioMapper;
import com.kauanallyson.rifa_hub_api.utils.mappers.RifaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RifaService {
    private final RifaRepository rifaRepository;
    private final RifaMapper rifaMapper;
    private final PremioMapper premioMapper;
    // Create
    @Transactional
    public RifaResponseDTO createRifa(RifaCreateDTO dto){
        if (rifaRepository.existsByNome(dto.nome())) {
            throw new DuplicateResourceException("Já existe uma rifa cadastrada com o nome: " + dto.nome());
        }
        List<Integer> colocacoes = dto.premios().stream()
                .map(PremioCreateDTO::colocacao)
                .toList();
        Set<Integer> colocacoesUnicas = new HashSet<>(colocacoes);

        if (colocacoes.size() != colocacoesUnicas.size()) {
            throw new BusinessException("Não é permitido cadastrar prêmios com a mesma colocação.");
        }

        Rifa rifa = new Rifa();
        rifa.setNome(dto.nome());
        rifa.setDescricao(dto.descricao());
        rifa.setPontoPreco(dto.pontoPreco());
        rifa.setQuantidadePontos(dto.quantidadePontos());
        rifa.setDataSorteio(dto.dataSorteio());
        rifa.setStatus(StatusRifa.ABERTA);

        List<Premio> premios = dto.premios().stream()
                .map(premioDTO -> premioMapper.toEntity(premioDTO, rifa))
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
        Rifa rifaSalva = rifaRepository.save(rifa);
        return rifaMapper.toResponseDTO(rifaSalva);
    }

    // Get all
    @Transactional(readOnly = true)
    public Page<RifaResponseDTO> getAllRifas(Pageable pageable){
        Page<Rifa> rifasPaginadas = rifaRepository.findAll(pageable);
        return rifasPaginadas.map(rifaMapper::toResponseDTO);
    }

    // Find by id
    @Transactional(readOnly = true)
    public RifaResponseDTO findRifaById(Long id){
        Rifa rifa= rifaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + id + " não encontrada"));
        return rifaMapper.toResponseDTO(rifa);
    }

    // Update
    @Transactional
    public RifaResponseDTO updateRifa(RifaUpdateDTO dto, Long id){
        Rifa rifa = rifaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + id + " não encontrada"));

        if (rifa.getStatus() != StatusRifa.ABERTA){
            throw new BusinessException("Apenas rifas com status 'ABERTA' podem ser editadas");
        }

        Optional<Rifa> rifaComMesmoNome = rifaRepository.findByNome(dto.nome());
        if (rifaComMesmoNome.isPresent() && !rifaComMesmoNome.get().getId().equals(id)){
            throw new DuplicateResourceException("Já existe outra rifa cadastrada com o nome: " + dto.nome());
        }

        rifa.setNome(dto.nome());
        rifa.setDescricao(dto.descricao());
        rifa.setPontoPreco(dto.pontoPreco());
        rifa.setDataSorteio(dto.dataSorteio());

        Rifa rifaAtualizada = rifaRepository.save(rifa);
        return rifaMapper.toResponseDTO(rifaAtualizada);
    }

    // Delete
    @Transactional
    public void deleteRifa(Long id){
        Rifa rifa = rifaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + id + " não encontrada"));

        if (rifa.getStatus() == StatusRifa.FINALIZADA){
            throw new BusinessException("Rifas com status 'FINALIZADAS' não podem ser canceladas");
        }
        if (rifa.getStatus() == StatusRifa.CANCELADA) {
            throw new BusinessException("Esta rifa já está cancelada.");
        }

        boolean temPontosVendidos = rifa.getPontos().stream()
                .anyMatch(ponto -> ponto.getStatus() != StatusPonto.DISPONIVEL);
        if (temPontosVendidos) {
            throw new BusinessException("Não é possível cancelar uma rifa que já possui pontos vendidos ou reservados.");
        }

        rifa.setStatus(StatusRifa.CANCELADA);
        rifaRepository.save(rifa);
    }
}
