package com.Seia.services;

import com.Seia.dtos.DadosApoioDTO;
import com.Seia.dtos.PlanoApoioDTO;
import com.Seia.models.DadosApoio;
import com.Seia.models.PlanosApoio;
import com.Seia.models.Usuario;
import com.Seia.repositories.DadosApoioRepository;
import com.Seia.repositories.PlanoApoioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class PlanoApoioService {

    private static final long TAMANHO_MAXIMO_ANEXO = 10L * 1024 * 1024;
    private static final Set<String> EXTENSOES_PERMITIDAS =
            Set.of(".pdf", ".txt", ".md", ".png", ".jpg", ".jpeg");
    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            "application/pdf", "text/plain", "text/markdown", "image/png", "image/jpeg", "application/octet-stream"
    );

    private final PlanoApoioRepository planoApoioRepository;
    private final DadosApoioRepository dadosApoioRepository;
    private final GeminiApiService geminiService;
    private final HigienizadorHtmlPlano higienizadorHtml;

    public PlanoApoioService(PlanoApoioRepository planoApoioRepository,
                             DadosApoioRepository dadosApoioRepository,
                             GeminiApiService geminiService,
                             HigienizadorHtmlPlano higienizadorHtml) {
        this.planoApoioRepository = planoApoioRepository;
        this.dadosApoioRepository = dadosApoioRepository;
        this.geminiService = geminiService;
        this.higienizadorHtml = higienizadorHtml;
    }

    public PlanoApoioDTO gerarESalvarPlano(Long dadosApoioId, MultipartFile arquivoContexto) {
        validarArquivoContexto(arquivoContexto);

        DadosApoio dados = dadosApoioRepository.findByIdAndUsuarioId(dadosApoioId, usuarioAtualId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contexto acadêmico não encontrado."));

        String htmlGerado;
        try {
            htmlGerado = geminiService.gerarPlanoDeApoioHtml(
                    dados.getNome(),
                    dados.getMateria(),
                    dados.getSerie(),
                    dados.getConteudos(),
                    dados.getBarreiras(),
                    dados.getSolucaoNecessaria(),
                    arquivoContexto
            );
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Não foi possível obter um plano válido da IA.", exception);
        }

        PlanosApoio plano = new PlanosApoio();
        plano.setConteudoGeradoIa(higienizadorHtml.higienizar(htmlGerado));
        plano.setDadosApoio(dados);
        plano.setUsuario(dados.getUsuario());

        return paraDTO(planoApoioRepository.save(plano));
    }

    public PlanoApoioDTO editarPlanoApoio(Long id, String htmlEditado) {
        String htmlSeguro = higienizarEdicao(htmlEditado);
        PlanosApoio planoExistente = buscarEntidadeDoUsuario(id);
        planoExistente.setConteudoGeradoIa(htmlSeguro);
        return paraDTO(planoApoioRepository.save(planoExistente));
    }

    public List<PlanoApoioDTO> listarPlanosDoUsuarioAtual() {
        return planoApoioRepository.findByUsuarioId(usuarioAtualId()).stream()
                .map(this::paraDTO)
                .toList();
    }

    public PlanoApoioDTO buscarPorId(Long idPlano) {
        return paraDTO(buscarEntidadeDoUsuario(idPlano));
    }

    @Transactional
    public void excluirPlano(Long idPlano) {
        PlanosApoio plano = buscarEntidadeDoUsuario(idPlano);
        DadosApoio contexto = plano.getDadosApoio();
        planoApoioRepository.delete(plano);
        planoApoioRepository.flush();
        if (!planoApoioRepository.existsByDadosApoioId(contexto.getId())) {
            dadosApoioRepository.delete(contexto);
        }
    }

    private PlanoApoioDTO paraDTO(PlanosApoio plano) {
        return new PlanoApoioDTO(
                plano.getId(),
                higienizadorHtml.higienizar(plano.getConteudoGeradoIa()),
                DadosApoioDTO.toDTO(plano.getDadosApoio())
        );
    }

    private PlanosApoio buscarEntidadeDoUsuario(Long idPlano) {
        return planoApoioRepository.findByIdAndUsuarioId(idPlano, usuarioAtualId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado."));
    }

    private Integer usuarioAtualId() {
        var autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao != null && autenticacao.getPrincipal() instanceof Usuario usuario) {
            return usuario.getId();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
    }

    private void validarArquivoContexto(MultipartFile arquivo) {
        if (arquivo == null) return;
        if (arquivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O arquivo de contexto está vazio.");
        }
        if (arquivo.getSize() > TAMANHO_MAXIMO_ANEXO) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "O arquivo de contexto excede 10 MB.");
        }

        String nome = arquivo.getOriginalFilename() == null
                ? ""
                : arquivo.getOriginalFilename().toLowerCase(Locale.ROOT);
        String tipo = arquivo.getContentType() == null
                ? "application/octet-stream"
                : arquivo.getContentType().toLowerCase(Locale.ROOT);
        tipo = tipo.split(";", 2)[0].trim();
        boolean extensaoValida = EXTENSOES_PERMITIDAS.stream().anyMatch(nome::endsWith);
        if (!extensaoValida || !TIPOS_PERMITIDOS.contains(tipo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Formato de anexo não permitido. Use PDF, TXT, Markdown, PNG ou JPG.");
        }
    }

    private String higienizarEdicao(String html) {
        if (html == null || html.isBlank() || html.length() > 100_000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O conteúdo editado é inválido.");
        }
        String htmlSeguro = higienizadorHtml.higienizar(html);
        if (htmlSeguro.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O conteúdo editado está vazio.");
        }
        return htmlSeguro;
    }
}
