package com.Seia.dtos;

import com.Seia.models.DadosApoio;

public record DadosApoioDTO(String nome, String materia, String serie, String conteudos, String barreiras, String solucaoNecessaria) {

    public DadosApoio toEntity() {

        DadosApoio dadosApoio = new DadosApoio();

        dadosApoio.setNome(nome());
        dadosApoio.setMateria(materia());
        dadosApoio.setSerie(serie());
        dadosApoio.setConteudos(conteudos());
        dadosApoio.setBarreiras(barreiras());
        dadosApoio.setSolucaoNecessaria(solucaoNecessaria());

        return dadosApoio;
    }

    public static DadosApoioDTO toDTO(DadosApoio dadosApoio) {
        return new DadosApoioDTO(dadosApoio.getNome(), dadosApoio.getMateria(), dadosApoio.getSerie(), dadosApoio.getConteudos(), dadosApoio.getBarreiras(), dadosApoio.getSolucaoNecessaria());
    }
}
