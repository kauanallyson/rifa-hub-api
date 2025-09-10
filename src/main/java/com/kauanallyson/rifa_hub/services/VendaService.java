package com.kauanallyson.rifa_hub.services;

import com.kauanallyson.rifa_hub.entities.Venda;
import com.kauanallyson.rifa_hub.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub.repositories.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VendaService {
    private final VendaRepository vendaRepository;

    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    @Transactional
    public Venda createVenda(Venda venda) {
        return vendaRepository.save(venda);
    }

    @Transactional(readOnly = true)
    public List<Venda> findAllVendas() {
        return vendaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Venda findVendaById(Long id) {
        return vendaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venda com id " + id + " não encontrado"));
    }

    @Transactional
    public Venda updateVenda(Long id, Venda vendaDetails){
        Venda existingVenda = findVendaById(id);

        existingVenda.setNomeCliente(vendaDetails.getNomeCliente());
        existingVenda.setEmailCliente(vendaDetails.getEmailCliente());

        return vendaRepository.save(existingVenda);
    }

    @Transactional
    public void deleteVenda(Long id){
        if (!vendaRepository.existsById(id)){
            throw new ResourceNotFoundException("Venda com id " + id + " não encontrada");
        }
        vendaRepository.deleteById(id);
    }
}
