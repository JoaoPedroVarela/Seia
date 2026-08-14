package com.Seia.controllers;

import com.Seia.dtos.PlanoApoioDTO;
import com.Seia.services.PlanoApoioService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/planos")
public class PlanoApoioController {

    private final PlanoApoioService planoApoioService;

    public PlanoApoioController(PlanoApoioService planoApoioService) {
        this.planoApoioService = planoApoioService;
    }

    @PostMapping(value = "/gerar/{dadosApoioId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlanoApoioDTO> gerarPlano(@PathVariable Long dadosApoioId,
                                                    @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
        PlanoApoioDTO planoGerado = planoApoioService.gerarESalvarPlano(dadosApoioId, arquivo);
        return ResponseEntity.ok(planoGerado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanoApoioDTO> editarPlano(@PathVariable Long id, @RequestBody String htmlEditado) {
        PlanoApoioDTO planoAtualizado = planoApoioService.editarPlanoApoio(id, htmlEditado);
        return ResponseEntity.ok(planoAtualizado);
    }

    @GetMapping("/meus-planos")
    public ResponseEntity<List<PlanoApoioDTO>> listaPlanosDeApoio() {
        return ResponseEntity.ok(planoApoioService.listarPlanosDoUsuarioAtual());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoApoioDTO> buscarPlanoPorId(@PathVariable Long id) {
        PlanoApoioDTO plano = planoApoioService.buscarPorId(id);
        return ResponseEntity.ok(plano);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPlano(@PathVariable Long id) {
        planoApoioService.excluirPlano(id);
        return ResponseEntity.noContent().build();
    }
}
