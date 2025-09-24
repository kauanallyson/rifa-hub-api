package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.comprador.CompradorCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.comprador.CompradorResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.comprador.CompradorUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.Comprador;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.CompradorRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.CompradorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompradorService {
    private final CompradorRepository compradorRepository;
    private final CompradorMapper compradorMapper;

    // Create
    @Transactional
    public CompradorResponseDTO createComprador(CompradorCreateDTO dto) {
        if (dto.email() != null && !dto.email().isEmpty() && compradorRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Este e-mail não está disponível para uso.");
        }

        if (compradorRepository.existsByTelefone(dto.telefone())) {
            throw new DuplicateResourceException("Este telefone não está disponível para uso.");
        }

        Comprador comprador = compradorMapper.toEntity(dto);
        Comprador compradorSalvo = compradorRepository.save(comprador);
        return compradorMapper.toResponseDTO(compradorSalvo);
    }

    // Get all Ativo
    @Transactional(readOnly = true)
    public Page<CompradorResponseDTO> getAllCompradores(Pageable pageable) {
        Page<Comprador> compradoresAtivos = compradorRepository.findAllByAtivoTrue(pageable);
        return compradoresAtivos.map(compradorMapper::toResponseDTO);
    }

    // Find by id and Ativo
    @Transactional(readOnly = true)
    public CompradorResponseDTO findCompradorById(Long id) {
        Comprador comprador = compradorRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comprador com id " + id + " não encontrado ou inativo."));
        return compradorMapper.toResponseDTO(comprador);
    }

    // Find by nome and Ativo
    @Transactional(readOnly = true)
    public Page<CompradorResponseDTO> findCompradorByNome(String nome, Pageable pageable) {
        Page<Comprador> compradores = compradorRepository.findAllByNomeContainingIgnoreCaseAndAtivoTrue(nome, pageable);
        return compradores.map(compradorMapper::toResponseDTO);
    }

    // Update
    @Transactional
    public CompradorResponseDTO updateComprador(Long id, CompradorUpdateDTO dto) {
        Comprador comprador = compradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comprador com id " + id + " não encontrado"));

        if (!comprador.isAtivo()) {
            throw new BusinessException("Não é possível alterar um comprador inativo.");
        }

        Optional<Comprador> compradorComMesmoEmail = compradorRepository.findByEmail(dto.email());
        if (dto.email() != null && !dto.email().isEmpty() && compradorComMesmoEmail.isPresent() && !compradorComMesmoEmail.get().getId().equals(id)) {
            throw new DuplicateResourceException("Este e-mail não está disponível para uso.");
        }

        Optional<Comprador> compradorComMesmoTelefone = compradorRepository.findByTelefone(dto.telefone());
        if (compradorComMesmoTelefone.isPresent() && !compradorComMesmoTelefone.get().getId().equals(id)) {
            throw new DuplicateResourceException("Este telefone não está disponível para uso.");
        }

        compradorMapper.updateEntityFromDTO(dto, comprador);
        compradorRepository.save(comprador);
        return compradorMapper.toResponseDTO(comprador);
    }

    // Delete
    @Transactional
    public void deleteComprador(Long id) {
        Comprador comprador = compradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comprador com id " + id + " não encontrado"));

        if (!comprador.isAtivo()) {
            throw new BusinessException("Este comprador já se encontra inativo.");
        }

        comprador.setAtivo(false);
        compradorRepository.save(comprador);
    }
}