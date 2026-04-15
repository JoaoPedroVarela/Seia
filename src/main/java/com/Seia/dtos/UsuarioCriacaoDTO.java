package com.Seia.dtos;

import com.Seia.models.Usuario;

public record UsuarioCriacaoDTO(String nome, String email, String senha) {

    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setNome(nome());
        usuario.setEmail(email());
        usuario.setSenha(senha());

        return usuario;
    }
}
