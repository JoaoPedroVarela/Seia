package com.Seia.controllers;

import com.Seia.dtos.PlanoApoioDTO;
import com.Seia.models.PlanosApoio;
import com.Seia.services.PlanoApoioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planos")
public class PlanoApoioController {

    private final PlanoApoioService planoApoioService;

    public PlanoApoioController(PlanoApoioService planoApoioService) {
        this.planoApoioService = planoApoioService;
    }

    @PostMapping("/gerar/{dadosApoioId}")
    public ResponseEntity<PlanoApoioDTO> gerarPlano(@PathVariable Long dadosApoioId) {
        PlanoApoioDTO planoGerado = planoApoioService.gerarESalvarPlano(dadosApoioId);
        return ResponseEntity.ok(planoGerado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanoApoioDTO> editarPlano(@PathVariable Long id, @RequestBody String htmlEditado) {
        PlanoApoioDTO planoAtualizado = planoApoioService.editarPlanoApoio(id, htmlEditado);
        return ResponseEntity.ok(planoAtualizado);
    }

    @GetMapping("/listaPlanosPorUsuario/{usuarioId}")
    public ResponseEntity<List<PlanoApoioDTO>> listaPlanosDeApoio(@PathVariable Long usuarioId) {
        List<PlanoApoioDTO> lista = planoApoioService.listarPlanosPorUsuario(usuarioId);
        return ResponseEntity.ok(lista);
    }
}