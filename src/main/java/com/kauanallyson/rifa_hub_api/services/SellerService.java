package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dtos.seller.SellerCreateDTO;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerResponseDTO;
import com.kauanallyson.rifa_hub_api.dtos.seller.SellerUpdateDTO;
import com.kauanallyson.rifa_hub_api.entities.Seller;
import com.kauanallyson.rifa_hub_api.exceptions.BusinessException;
import com.kauanallyson.rifa_hub_api.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.repositories.SellerRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.SellerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    // Create
    @Transactional
    public SellerResponseDTO createSeller(SellerCreateDTO dto) {
        if (sellerRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Email not available");
        }

        if (sellerRepository.existsByPhone(dto.phone())) {
            throw new DuplicateResourceException("Phone not available");
        }

        Seller seller = sellerMapper.toEntity(dto);
        Seller savedSeller = sellerRepository.save(seller);
        return sellerMapper.toResponseDTO(savedSeller);
    }

    // Get all Active
    @Transactional(readOnly = true)
    public List<SellerResponseDTO> getAllSellers() {
        List<Seller> activeSellers = sellerRepository.findAllActive();
        return activeSellers.stream()
                .map(sellerMapper::toResponseDTO)
                .toList();
    }

    // Find Active By id
    @Transactional(readOnly = true)
    public SellerResponseDTO findSellerById(Long id) {
        Seller seller = sellerRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor com id " + id + " não encontrado"));
        return sellerMapper.toResponseDTO(seller);
    }

    // Find Active By Name
    @Transactional(readOnly = true)
    public List<SellerResponseDTO> findSellerByName(String nome) {
        List<Seller> vendedores = sellerRepository.findAllActiveByName(nome);
        return vendedores.stream()
                .map(sellerMapper::toResponseDTO)
                .toList();
    }

    // Update
    @Transactional
    public SellerResponseDTO updateSeller(Long id, SellerUpdateDTO dto) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with id " + id + " not found"));

        if (!seller.isActive()) {
            throw new BusinessException("Seller inactive");
        }

        Optional<Seller> vendedorComMesmoEmail = sellerRepository.findByEmail(dto.email());
        if (vendedorComMesmoEmail.isPresent() && !vendedorComMesmoEmail.get().getId().equals(id)) {
            throw new DuplicateResourceException("Email not available");
        }

        Optional<Seller> vendedorComMesmoTelefone = sellerRepository.findByPhone(dto.phone());
        if (vendedorComMesmoTelefone.isPresent() && !vendedorComMesmoTelefone.get().getId().equals(id)) {
            throw new DuplicateResourceException("Phone not available");
        }

        sellerMapper.updateEntityFromDTO(dto, seller);
        sellerRepository.save(seller);
        return sellerMapper.toResponseDTO(seller);
    }

    // Delete
    @Transactional
    public void deleteSeller(Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with " + id + " not found"));

        if (!seller.isActive()) {
            throw new BusinessException("Seller already inactive");
        }

        seller.setActive(false);
        sellerRepository.save(seller);
    }
}