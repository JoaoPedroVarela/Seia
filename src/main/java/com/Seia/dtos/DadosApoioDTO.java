package com.Seia.dtos;

import com.Seia.models.DadosApoio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosApoioDTO(
        @NotBlank @Size(min = 3, max = 100) String nome,
        @NotBlank @Size(min = 3, max = 100) String materia,
        @NotBlank @Size(min = 2, max = 100) String serie,
        @NotBlank @Size(min = 10, max = 2000) String conteudos,
        @NotBlank @Size(min = 10, max = 4000) String barreiras,
        @Size(max = 4000) String solucaoNecessaria) {

    public DadosApoio toEntity() {

        DadosApoio dadosApoio = new DadosApoio();

        dadosApoio.setNome(nome().trim());
        dadosApoio.setMateria(materia().trim());
        dadosApoio.setSerie(serie().trim());
        dadosApoio.setConteudos(conteudos().trim());
        dadosApoio.setBarreiras(barreiras().trim());
        dadosApoio.setSolucaoNecessaria(solucaoNecessaria() == null ? "" : solucaoNecessaria().trim());

        return dadosApoio;
    }

    public static DadosApoioDTO toDTO(DadosApoio dadosApoio) {
        return new DadosApoioDTO(dadosApoio.getNome(), dadosApoio.getMateria(), dadosApoio.getSerie(), dadosApoio.getConteudos(), dadosApoio.getBarreiras(), dadosApoio.getSolucaoNecessaria());
    }
}
