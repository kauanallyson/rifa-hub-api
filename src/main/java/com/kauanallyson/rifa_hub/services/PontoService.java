package com.kauanallyson.rifa_hub.services;

import com.kauanallyson.rifa_hub.entities.Ponto;
import com.kauanallyson.rifa_hub.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub.repositories.PontoRepository;
import com.kauanallyson.rifa_hub.repositories.RifaRepository;
import com.kauanallyson.rifa_hub.repositories.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PontoService {
    private final PontoRepository pontoRepository;
    private final RifaRepository rifaRepository;
    private final VendaRepository vendaRepository;

    public PontoService(PontoRepository pontoRepository, RifaRepository rifaRepository, VendaRepository vendaRepository) {
        this.pontoRepository = pontoRepository;
        this.rifaRepository = rifaRepository;
        this.vendaRepository = vendaRepository;
    }

    @Transactional(readOnly = true)
    public Ponto findPontoById(Long id) {
        return pontoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ponto com id " + id + " não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Ponto> findAllPontosByRifa(Long rifaId) {
        if (rifaRepository.findById(rifaId).isEmpty()) {
            throw new ResourceNotFoundException("Rifa com id " + rifaId + " não encontrada");
        }
        return pontoRepository.findByRifaId(rifaId);

    }

    @Transactional(readOnly = true)
    public List<Ponto> findAllPontosByVenda(Long vendaId) {
        if (vendaRepository.findById(vendaId).isEmpty()) {
            throw new ResourceNotFoundException("Venda com id " + vendaId + " não encontrada");
        }
        return pontoRepository.findByVendaId(vendaId);
    }

    @Transactional(readOnly = true)
    public boolean isPontoAvailable(Long rifaId, Integer numero) {
        if (!rifaRepository.existsById(rifaId)) {
            throw new ResourceNotFoundException("Rifa com id " + rifaId + " não encontrada");
        }
        return !pontoRepository.existsByRifaIdAndNumero(rifaId, numero);
    }

    @Transactional
    void savePonto(Ponto ponto) {
        pontoRepository.save(ponto);
    }
}
