package com.Seia.services;

import com.Seia.dtos.DadosApoioDTO;
import com.Seia.models.DadosApoio;
import com.Seia.models.Usuario;
import com.Seia.repositories.DadosApoioRepository;
import com.Seia.repositories.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DadosApoioService {

    private final DadosApoioRepository dadosApoioRepository;
    private final UsuarioRepository usuarioRepository;

    public DadosApoioService(DadosApoioRepository dadosApoioRepository, UsuarioRepository usuarioRepository) {
        this.dadosApoioRepository = dadosApoioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Long save(DadosApoioDTO dto) {
        DadosApoio dadosApoio = dto.toEntity();

        String emailDoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuarioLogado = usuarioRepository.findByEmail(emailDoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        dadosApoio.setUsuario(usuarioLogado);
        DadosApoio salvo = dadosApoioRepository.save(dadosApoio);
        return salvo.getId();
    }

    public void delete(Long id) {
        this.dadosApoioRepository.deleteById(id);
    }

}
