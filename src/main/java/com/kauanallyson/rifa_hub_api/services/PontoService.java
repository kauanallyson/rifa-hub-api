package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.ponto.PontoResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.ponto.PontoVendaRequestDTO;
import com.kauanallyson.rifa_hub_api.entities.Comprador;
import com.kauanallyson.rifa_hub_api.entities.Ponto;
import com.kauanallyson.rifa_hub_api.entities.Rifa;
import com.kauanallyson.rifa_hub_api.entities.Vendedor;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusPonto;
import com.kauanallyson.rifa_hub_api.entities.enums.StatusRifa;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.CompradorRepository;
import com.kauanallyson.rifa_hub_api.repositories.PontoRepository;
import com.kauanallyson.rifa_hub_api.repositories.RifaRepository;
import com.kauanallyson.rifa_hub_api.repositories.VendedorRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.PontoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PontoService {

    private final PontoRepository pontoRepository;
    private final RifaRepository rifaRepository;
    private final CompradorRepository compradorRepository;
    private final VendedorRepository vendedorRepository;
    private final PontoMapper pontoMapper;

    @Transactional
    public PontoResponseDTO venderPonto(Long rifaId, Integer numeroPonto, PontoVendaRequestDTO dto) {
        Rifa rifa = rifaRepository.findById(rifaId)
                .orElseThrow(() -> new ResourceNotFoundException("Rifa com id " + rifaId + " não encontrada."));

        Comprador comprador = compradorRepository.findByIdAndAtivoTrue(dto.compradorId())
                .orElseThrow(() -> new ResourceNotFoundException("Comprador com id " + dto.compradorId() + " não encontrado ou inativo."));

        Vendedor vendedor = vendedorRepository.findByIdAndAtivoTrue(dto.vendedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor com id " + dto.vendedorId() + " não encontrado ou inativo."));

        Ponto ponto = pontoRepository.findByRifaIdAndNumero(rifaId, numeroPonto)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de número " + numeroPonto + " não encontrado para esta rifa."));

        if (rifa.getStatus() != StatusRifa.ABERTA) {
            throw new BusinessException("Esta rifa não está aberta para vendas.");
        }
        if (ponto.getStatus() != StatusPonto.DISPONIVEL) {
            throw new BusinessException("O ponto de número " + numeroPonto + " não está mais disponível.");
        }

        ponto.setStatus(StatusPonto.VENDIDO);
        ponto.setComprador(comprador);
        ponto.setVendedor(vendedor);
        ponto.setDataVenda(LocalDateTime.now());

        Ponto pontoVendido = pontoRepository.save(ponto);
        return pontoMapper.toResponseDTO(pontoVendido);
    }

    @Transactional(readOnly = true)
    public Page<PontoResponseDTO> getPontosByRifa(Long rifaId, Pageable pageable) {
        if (!rifaRepository.existsById(rifaId)) {
            throw new ResourceNotFoundException("Rifa com id " + rifaId + " não encontrada.");
        }
        Page<Ponto> pontos = pontoRepository.findAllByRifaId(rifaId, pageable);
        return pontos.map(pontoMapper::toResponseDTO);
    }
}