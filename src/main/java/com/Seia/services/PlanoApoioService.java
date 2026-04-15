package com.Seia.services;

import com.Seia.dtos.PlanoApoioDTO;
import com.Seia.models.DadosApoio;
import com.Seia.models.PlanosApoio;
import com.Seia.repositories.DadosApoioRepository;
import com.Seia.repositories.PlanoApoioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlanoApoioService {

    private final PlanoApoioRepository planoApoioRepository;
    private final DadosApoioRepository dadosApoioRepository;
    private final GeminiApiService geminiService;

    public PlanoApoioService(PlanoApoioRepository planoApoioRepository,
                             DadosApoioRepository dadosApoioRepository,
                             GeminiApiService geminiService) {
        this.planoApoioRepository = planoApoioRepository;
        this.dadosApoioRepository = dadosApoioRepository;
        this.geminiService = geminiService;
    }

    public PlanoApoioDTO gerarESalvarPlano(Long dadosApoioId) {

        DadosApoio dados = dadosApoioRepository.findById(dadosApoioId)
                .orElseThrow(() -> new RuntimeException("Dados de apoio não encontrados!"));

        String htmlGerado = geminiService.gerarPlanoDeApoioHtml(
                dados.getNome(),
                dados.getMateria(),
                dados.getSerie(),
                dados.getConteudos(),
                dados.getBarreiras(),
                dados.getSolucaoNecessaria()
        );

        PlanosApoio plano;

        plano = new PlanosApoio();
        plano.setConteudoGeradoIa(htmlGerado);
        plano.setDadosApoio(dados);
        plano.setUsuario(dados.getUsuario());

        PlanosApoio planoSalvo = planoApoioRepository.save(plano);

        return new PlanoApoioDTO(
                planoSalvo.getId(),
                planoSalvo.getConteudoGeradoIa()
        );
    }

    public PlanoApoioDTO editarPlanoApoio(Long id, String htmlEditado) {

        PlanosApoio planoExistente = planoApoioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano de apoio não encontrado!"));
        planoExistente.setConteudoGeradoIa(htmlEditado);

        PlanosApoio planoAtualizado = planoApoioRepository.save(planoExistente);

        return new PlanoApoioDTO(
                planoAtualizado.getId(),
                planoAtualizado.getConteudoGeradoIa()
        );
    }

    public List<PlanoApoioDTO> listarPlanosPorUsuario(Long idUsuario) {
        List<PlanoApoioDTO> lista = new ArrayList<>();
        for(PlanosApoio planoApoio : planoApoioRepository.findByUsuarioId(idUsuario)) {
            lista.add(new PlanoApoioDTO(planoApoio.getId(), planoApoio.getConteudoGeradoIa()));
        }

        return lista;
    }
}