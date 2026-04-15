package com.Seia.services;

import com.Seia.dtos.DadosApoioDTO;
import com.Seia.models.DadosApoio;
import com.Seia.repositories.DadosApoioRepository;
import org.springframework.stereotype.Service;

@Service
public class DadosApoioService {

    private final DadosApoioRepository dadosApoioRepository;

    public DadosApoioService(DadosApoioRepository dadosApoioRepository) {
        this.dadosApoioRepository = dadosApoioRepository;
    }

    public Long save(DadosApoioDTO dadosApoioDTO) {
        return this.dadosApoioRepository.save(dadosApoioDTO.toEntity()).getId();
    }

    public void delete(Long id) {
        this.dadosApoioRepository.deleteById(id);
    }

}
