package com.Seia.services;

import com.Seia.dtos.UsuarioCriacaoDTO;
import com.Seia.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void save(UsuarioCriacaoDTO usuarioDto) {
        usuarioRepository.save(usuarioDto.toEntity());
    }

    public void delete(Long idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }
}
