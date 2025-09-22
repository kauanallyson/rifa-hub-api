package com.kauanallyson.rifa_hub_api.services;

import com.kauanallyson.rifa_hub_api.dto.vendedor.VendedorCreateDTO;
import com.kauanallyson.rifa_hub_api.dto.vendedor.VendedorResponseDTO;
import com.kauanallyson.rifa_hub_api.entities.Vendedor;
import com.kauanallyson.rifa_hub_api.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.repositories.VendedorRepository;
import com.kauanallyson.rifa_hub_api.utils.mappers.VendedorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VendedorService {
    private final VendedorRepository vendedorRepository;
    private final VendedorMapper vendedorMapper;

    @Transactional
    public VendedorResponseDTO createVendedor(VendedorCreateDTO dto) {
        if (vendedorRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Email: " + dto.email() + " já está em uso");
        }
        Vendedor vendedor = vendedorMapper.toEntity(dto);
        Vendedor vendedorSalvo = vendedorRepository.save(vendedor);

        return vendedorMapper.toResponseDTO(vendedorSalvo);
    }
}
