package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.premio.PremioCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.rifa.RifaCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.rifa.RifaResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.rifa.RifaUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.Ponto;
import com.kauanallyson.rifa_hub_api.entities.Premio;
import com.kauanallyson.rifa_hub_api.entities.Rifa;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusPonto;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.RifaRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.RifaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RifaService {
    private final RifaRepository rifaRepository;
    private final RifaMapper rifaMapper;

    // Create
    @Transactional
    public RifaResponseDTO createRifa(RifaCreateDTO dto) {
        if (rifaRepository.existsByNome(dto.nome())) {
            throw new DuplicateResourceException("Já existe uma rifa cadastrada com o nome: " + dto.nome());
        }
        List<Integer> colocacoes = dto.premios().stream().map(PremioCreateDTO::colocacao).toList();
        if (new HashSet<>(colocacoes).size() < colocacoes.size()) {
            throw new BusinessException("Não é permitido cadastrar prêmios com a mesma colocação.");
        }
        Rifa rifa = rifaMapper.toEntity(dto);

        // Gerar pontos
        List<Ponto> pontos = IntStream.rangeClosed(1, dto.quantidadePontos()).mapToObj(numero -> {
            Ponto ponto = new Ponto();
            ponto.setNumero(numero);
            ponto.setStatus(StatusPonto.DISPONIVEL);
            ponto.setRifa(rifa);
            return ponto;
        }).toList();
        rifa.setPontos(pontos);
        Rifa rifaSalva = rifaRepository.save(rifa);
        return rifaMapper.toResponseDTO(rifaSalva);
    }

    // Get all
    @Transactional(readOnly = true)
    public Page<RifaResponseDTO> getAllRifas(Pageable pageable) {
        Page<Rifa> rifasPaginadas = rifaRepository.findAll(pageable);
        return rifasPaginadas.map(rifaMapper::toResponseDTO);
    }

    // Get all by Status
    @Transactional(readOnly = true)
    public Page<RifaResponseDTO> getAllRifas(Pageable pageable, StatusRifa status) {
        Page<Rifa> rifasPaginadas = rifaRepository.findAllByStatus(status, pageable);
        return rifasPaginadas.map(rifaMapper::toResponseDTO);
    }

    // Find by Nome
    @Transactional(readOnly = true)
    public Page<RifaResponseDTO> findAllRifasByNome(Pageable pageable, String nome) {
        List<StatusRifa> statuses = List.of(StatusRifa.ABERTA, StatusRifa.FINALIZADA);
        Page<Rifa> rifas = rifaRepository.findAllByNomeContainingIgnoreCaseAndStatusIn(nome, statuses, pageable);
        return rifas.map(rifaMapper::toResponseDTO);
    }

    // Find by id
    @Transactional(readOnly = true)
    public RifaResponseDTO findRifaById(Long id) {
        Rifa rifa = rifaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + id + " não encontrada"));
        return rifaMapper.toResponseDTO(rifa);
    }

    // Update
    @Transactional
    public RifaResponseDTO updateRifa(RifaUpdateDTO dto, Long id) {
        Rifa rifa = rifaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + id + " não encontrada"));

        if (rifa.getStatus() != StatusRifa.ABERTA) {
            throw new BusinessException("Apenas rifas com status 'ABERTA' podem ser editadas");
        }

        Optional<Rifa> rifaComMesmoNome = rifaRepository.findByNome(dto.nome());
        if (rifaComMesmoNome.isPresent() && !rifaComMesmoNome.get().getId().equals(id)) {
            throw new DuplicateResourceException("Já existe outra rifa cadastrada com o nome: " + dto.nome());
        }

        rifaMapper.updateEntityFromDTO(dto, rifa);
        rifaRepository.save(rifa);
        return rifaMapper.toResponseDTO(rifa);
    }

    // Delete (Cancel)
    @Transactional
    public void deleteRifa(Long id) {
        Rifa rifa = rifaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + id + " não encontrada"));

        if (rifa.getStatus() == StatusRifa.FINALIZADA) {
            throw new BusinessException("Rifas com status 'FINALIZADAS' não podem ser canceladas");
        }
        if (rifa.getStatus() == StatusRifa.CANCELADA) {
            throw new BusinessException("Esta rifa já está cancelada.");
        }
        boolean temPontosVendidos = rifa.getPontos().stream().anyMatch(ponto -> ponto.getStatus() != StatusPonto.DISPONIVEL);
        if (temPontosVendidos) {
            throw new BusinessException("Não é possível cancelar uma rifa que já possui pontos vendidos ou reservados.");
        }

        rifa.setStatus(StatusRifa.CANCELADA);
        rifaRepository.save(rifa);
    }

    @Transactional
    public RifaResponseDTO realizarSorteio(Long id) {
        Rifa rifa = rifaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + id + " não encontrada"));

        if (rifa.getStatus() != StatusRifa.ABERTA) {
            throw new BusinessException("Somente rifas com status 'ABERTA' podem ser sorteadas");
        }

        if (rifa.getDataSorteio() != null && LocalDateTime.now().isBefore(rifa.getDataSorteio())) {
            throw new BusinessException("A data do sorteio ainda não chegou.");
        }

        List<Ponto> pontosVendidos = new java.util.ArrayList<>(rifa.getPontos().stream()
                .filter(ponto -> ponto.getStatus() == StatusPonto.VENDIDO)
                .toList());

        if (pontosVendidos.isEmpty()) {
            throw new BusinessException("Não é possível realizar o sorteio, pois nenhum ponto foi vendido.");
        }

        int numeroDePremios = rifa.getPremios().size();
        if (pontosVendidos.size() < numeroDePremios) {
            throw new BusinessException("Não há pontos vendidos suficientes para cobrir todos os prêmios.");
        }

        Collections.shuffle(pontosVendidos);
        List<Ponto> pontosSorteados = pontosVendidos.subList(0, numeroDePremios);

        for (int i = 0; i < numeroDePremios; i++) {
            Premio premio = rifa.getPremios().get(i);
            Ponto pontoVencedor = pontosSorteados.get(i);
            premio.setPontoVencedor(pontoVencedor);
        }

        rifa.setStatus(StatusRifa.FINALIZADA);
        Rifa rifaFinalizada = rifaRepository.save(rifa);
        return rifaMapper.toResponseDTO(rifaFinalizada);
    }
}