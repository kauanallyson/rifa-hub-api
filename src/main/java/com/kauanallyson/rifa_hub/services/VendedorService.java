package com.kauanallyson.rifa_hub.services;

import com.kauanallyson.rifa_hub.entities.Vendedor;
import com.kauanallyson.rifa_hub.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub.repositories.VendedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class VendedorService {
    private final VendedorRepository vendedorRepository;

    public VendedorService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    @org.springframework.transaction.annotation.Transactional
    public Vendedor createVendedor(Vendedor vendedor) {
        if (vendedorRepository.existsByEmail(vendedor.getEmail())) {
            throw new DuplicateResourceException("O e-mail '" + vendedor.getEmail() + "' já está em uso");
        }
        if (vendedorRepository.existsByTelefone(vendedor.getTelefone())) {
            throw new DuplicateResourceException("O telefone '" + vendedor.getTelefone() + "' já está em uso");
        }
        return vendedorRepository.save(vendedor);
    }

    @Transactional(readOnly = true)
    public List<Vendedor> findAllVendedores() {
        return vendedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vendedor findVendedorById(Long id) {
        return vendedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vendedor com id " + id + " não encontrado"));
    }

    @Transactional
    public Vendedor updateVendedor(Long id, Vendedor vendedorDetails) {
        Vendedor existingVendedor = findVendedorById(id);
        if (!Objects.equals(existingVendedor.getEmail(), vendedorDetails.getEmail()) && vendedorRepository.existsByEmail(vendedorDetails.getEmail())) {
            throw new DuplicateResourceException("O email '" + vendedorDetails.getEmail() + "' já está em uso");
        }
        if (!Objects.equals(existingVendedor.getTelefone(), vendedorDetails.getTelefone()) && vendedorRepository.existsByTelefone(vendedorDetails.getTelefone())) {
            throw new DuplicateResourceException("O telefone '" + vendedorDetails.getTelefone() + "' já está em uso");
        }

        existingVendedor.setNome(vendedorDetails.getNome());
        existingVendedor.setEmail(vendedorDetails.getEmail());
        existingVendedor.setTelefone(vendedorDetails.getTelefone());

        return vendedorRepository.save(existingVendedor);
    }

    @Transactional
    public void deleteVendedor(Long id) {
        if (!vendedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vendedor com id " + id + " não encontrado");
        }
        vendedorRepository.deleteById(id);
    }

}
