package com.Seia.services;

import com.Seia.dtos.DadosApoioDTO;
import com.Seia.models.DadosApoio;
import com.Seia.models.Usuario;
import com.Seia.repositories.DadosApoioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DadosApoioService {

    private final DadosApoioRepository dadosApoioRepository;

    public DadosApoioService(DadosApoioRepository dadosApoioRepository) {
        this.dadosApoioRepository = dadosApoioRepository;
    }

    public Long save(DadosApoioDTO dto) {
        DadosApoio dadosApoio = dto.toEntity();

        dadosApoio.setUsuario(usuarioAtual());
        DadosApoio salvo = dadosApoioRepository.save(dadosApoio);
        return salvo.getId();
    }

    public void delete(Long id) {
        Usuario usuario = usuarioAtual();
        var dados = dadosApoioRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contexto não encontrado."));
        dadosApoioRepository.delete(dados);
    }

    private Usuario usuarioAtual() {
        var autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao != null && autenticacao.getPrincipal() instanceof Usuario usuario) return usuario;
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
    }
}
