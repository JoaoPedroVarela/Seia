package com.Seia.controllers;

import com.Seia.dtos.DadosApoioDTO;
import com.Seia.services.DadosApoioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dadosApoio")
public class DadosApoioController {

    private final DadosApoioService dadosApoioService;

    public DadosApoioController(DadosApoioService dadosApoioService) {
        this.dadosApoioService = dadosApoioService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Long> cadastrarDadosApoio(@RequestBody DadosApoioDTO dadosApoioDTO) {
        Long idDadoCadastrado = dadosApoioService.save(dadosApoioDTO);
        return ResponseEntity.ok(idDadoCadastrado);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarDadosApoio(@PathVariable Long id) {
        dadosApoioService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
