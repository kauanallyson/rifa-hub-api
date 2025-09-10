package com.kauanallyson.rifa_hub.services;

import com.kauanallyson.rifa_hub.entities.Ponto;
import com.kauanallyson.rifa_hub.entities.Rifa;
import com.kauanallyson.rifa_hub.entities.Sorteio;
import com.kauanallyson.rifa_hub.entities.enums.SorteioStatus;
import com.kauanallyson.rifa_hub.exceptions.BusinessException;
import com.kauanallyson.rifa_hub.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub.repositories.PontoRepository;
import com.kauanallyson.rifa_hub.repositories.RifaRepository;
import com.kauanallyson.rifa_hub.repositories.SorteioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class SorteioService {
    private final SorteioRepository sorteioRepository;
    private final RifaService rifaService;
    private final PontoService pontoService;

    public SorteioService(SorteioRepository sorteioRepository, RifaService rifaService, PontoService pontoService) {
        this.sorteioRepository = sorteioRepository;
        this.rifaService = rifaService;
        this.pontoService = pontoService;
    }

    @Transactional(readOnly = true)
    public Sorteio findSorteioById(Long id) {
        return sorteioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sorteio com id " + id + " não encontrado"));
    }

    @Transactional
    public Sorteio scheduleSorteio(Long rifaId, LocalDateTime dataSorteio){
        Rifa rifa = rifaService.findRifaById(rifaId);

        if (rifa.getSorteio() != null){
            throw new DuplicateResourceException("Esta rifa já possui um sorteio agendado");
        }
        Sorteio novoSorteio = new Sorteio(rifa,dataSorteio);
        return sorteioRepository.save(novoSorteio);
    }

    @Transactional
    public Sorteio performSorteio(Long rifaId){
        Rifa rifa = rifaService.findRifaById(rifaId);
        Sorteio sorteio = rifa.getSorteio();

        if (sorteio == null){
            throw new ResourceNotFoundException("Nenhum agendamento de sorteio encontrado para a rifa com id " + rifaId);
        }
        if (sorteio.getStatus() != SorteioStatus.AGENDADO){
            throw new BusinessException("Sorteio não pode ser realizado. Status do sorteio atual: " + sorteio.getStatus());
        }
        List<Ponto> pontosParticipantes = pontoService.findAllPontosByRifa(rifaId);
        if (pontosParticipantes.isEmpty()){
            throw new BusinessException("Não é possível realizar um sorteio sem nenhum ponto vendido");
        }

        Random random = new Random();
        int indiceVencedor = random.nextInt(pontosParticipantes.size());
        Ponto pontoVencedor = pontosParticipantes.get(indiceVencedor);

        sorteio.performSorteio(pontoVencedor);

        return sorteioRepository.save(sorteio);
    }
    @Transactional
    public Sorteio cancelSorteio(Long sorteioId){
        Sorteio sorteio = findSorteioById(sorteioId);
        sorteio.cancel();

        return sorteioRepository.save(sorteio);
    }
}
