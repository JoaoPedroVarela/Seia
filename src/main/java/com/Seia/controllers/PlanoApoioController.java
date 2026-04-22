package com.Seia.controllers;

import com.Seia.dtos.PlanoApoioDTO;
import com.Seia.models.Usuario;
import com.Seia.repositories.UsuarioRepository;
import com.Seia.services.PlanoApoioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planos")
public class PlanoApoioController {

    private final PlanoApoioService planoApoioService;
    private final UsuarioRepository usuarioRepository;

    public PlanoApoioController(PlanoApoioService planoApoioService, UsuarioRepository usuarioRepository) {
        this.planoApoioService = planoApoioService;
        this.usuarioRepository = usuarioRepository;
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

    @GetMapping("/meus-planos")
    public ResponseEntity<List<PlanoApoioDTO>> listaPlanosDeApoio() {

        String emailDoToken = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByEmail(emailDoToken)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        List<PlanoApoioDTO> lista = planoApoioService.listarPlanosPorUsuario(usuario.getId());

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoApoioDTO> buscarPlanoPorId(@PathVariable Long id) {
        PlanoApoioDTO plano = planoApoioService.buscarPorId(id);
        return ResponseEntity.ok(plano);
    }
}