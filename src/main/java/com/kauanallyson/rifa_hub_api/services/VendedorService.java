package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.vendedor.VendedorCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.vendedor.VendedorResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.vendedor.VendedorUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.Vendedor;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.VendedorRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.VendedorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendedorService {
    private final VendedorRepository vendedorRepository;
    private final VendedorMapper vendedorMapper;

    // Create
    @Transactional
    public VendedorResponseDTO createVendedor(VendedorCreateDTO dto) {
        if (vendedorRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Este e-mail não está disponível para uso.");
        }

        if (vendedorRepository.existsByTelefone(dto.telefone())) {
            throw new DuplicateResourceException("Este telefone não está disponível para uso.");
        }

        Vendedor vendedor = vendedorMapper.toEntity(dto);
        Vendedor vendedorSalvo = vendedorRepository.save(vendedor);
        return vendedorMapper.toResponseDTO(vendedorSalvo);
    }

    // Get all Ativo
    @Transactional(readOnly = true)
    public Page<VendedorResponseDTO> getAllVendedores(Pageable pageable) {
        Page<Vendedor> vendedoresAtivos = vendedorRepository.findAllByAtivoTrue(pageable);
        return vendedoresAtivos.map(vendedorMapper::toResponseDTO);
    }

    // Find by id and Ativo
    @Transactional(readOnly = true)
    public VendedorResponseDTO findVendedorById(Long id) {
        Vendedor vendedor = vendedorRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor com id " + id + " não encontrado"));
        return vendedorMapper.toResponseDTO(vendedor);
    }

    // Find by nome and Ativo
    @Transactional(readOnly = true)
    public Page<VendedorResponseDTO> findVendedorByNome(String nome, Pageable pageable) {
        Page<Vendedor> vendedores = vendedorRepository.findAllByNomeContainingIgnoreCaseAndAtivoTrue(nome, pageable);
        return vendedores.map(vendedorMapper::toResponseDTO);
    }

    // Update
    @Transactional
    public VendedorResponseDTO updateVendedor(VendedorUpdateDTO dto, Long id) {
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor com id " + id + " não encontrado"));

        if (!vendedor.isAtivo()) {
            throw new BusinessException("Não é possível alterar um vendedor inativo.");
        }

        Optional<Vendedor> vendedorComMesmoEmail = vendedorRepository.findByEmail(dto.email());
        if (vendedorComMesmoEmail.isPresent() && !vendedorComMesmoEmail.get().getId().equals(id)) {
            throw new DuplicateResourceException("Este e-mail não está disponível para uso.");
        }

        Optional<Vendedor> vendedorComMesmoTelefone = vendedorRepository.findByTelefone(dto.telefone());
        if (vendedorComMesmoTelefone.isPresent() && !vendedorComMesmoTelefone.get().getId().equals(id)) {
            throw new DuplicateResourceException("Este telefone não está disponível para uso.");
        }

        vendedorMapper.updateEntityFromDTO(dto, vendedor);
        vendedorRepository.save(vendedor);
        return vendedorMapper.toResponseDTO(vendedor);
    }

    // Delete
    @Transactional
    public void deleteVendedor(Long id) {
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor com id " + id + " não encontrado"));

        if (!vendedor.isAtivo()) {
            throw new BusinessException("Este vendedor já se encontra inativo.");
        }

        vendedor.setAtivo(false);
        vendedorRepository.save(vendedor);
    }
}