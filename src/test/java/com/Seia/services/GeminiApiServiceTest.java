package com.Seia.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GeminiApiServiceTest {

    private static final String JSON_VALIDO = """
            {
              "sinteseContexto": "Plano para Cálculo I com foco em derivadas.",
              "premissas": ["O cronograma precisa de validação docente."],
              "informacoesAusentes": [],
              "mapaConteudos": [{
                "tema": "Derivadas",
                "prioridade": "ESSENCIAL",
                "origem": "CONTEXTO",
                "justificativa": "É o objetivo central informado.",
                "prerequisitos": ["Funções"],
                "resultadoEsperado": "Aplicar regras de derivação."
              }],
              "revisoesPrerequisitos": [{
                "conteudo": "Funções",
                "atividades": ["Revisar domínio e imagem."],
                "criterioDominio": "Resolver quatro de cinco itens."
              }],
              "objetivosAprendizagem": [
                {"descricao": "Identificar regras de derivação.", "nivelBloom": "Compreender", "evidencia": "Classifica exemplos."},
                {"descricao": "Aplicar regras de derivação.", "nivelBloom": "Aplicar", "evidencia": "Resolve exercícios."},
                {"descricao": "Analisar soluções de derivadas.", "nivelBloom": "Analisar", "evidencia": "Justifica os passos."}
              ],
              "roteiroSemanal": [
                {
                  "periodo": "Semana 1",
                  "objetivo": "Revisar funções.",
                  "conteudos": ["Funções"],
                  "atividadesEstudo": ["Construir um mapa conceitual."],
                  "exercicios": ["Resolver questões diagnósticas."],
                  "apoiosAdaptacoes": ["Usar exemplos graduais."],
                  "evidenciasProgresso": ["Explica domínio e imagem."],
                  "criterioAvanco": "Atingir quatro de cinco itens."
                },
                {
                  "periodo": "Semana 2",
                  "objetivo": "Aplicar derivadas.",
                  "conteudos": ["Regras de derivação"],
                  "atividadesEstudo": ["Estudar exemplos resolvidos."],
                  "exercicios": ["Resolver problemas graduados."],
                  "apoiosAdaptacoes": ["Receber dicas progressivas."],
                  "evidenciasProgresso": ["Justifica a regra escolhida."],
                  "criterioAvanco": "Resolver exercícios sem dicas."
                }
              ],
              "apoiosPorBarreira": [{
                "barreira": "Dificuldade de interpretação",
                "impacto": "Dificulta escolher a operação correta.",
                "acoes": ["Destacar dados e pedido do enunciado."],
                "principiosPedagogicos": ["Andaimes graduais"],
                "recursos": ["Roteiro de leitura"],
                "evidenciasProgresso": ["Identifica dados relevantes."],
                "acompanhamento": ["Feedback semanal."]
              }],
              "praticaComIa": {
                "orientacoesUso": ["Anexar o material de referência.", "Tentar antes de pedir o gabarito."],
                "promptExercicios": "Gere cinco exercícios usando somente o material anexado e apresente dicas progressivas.",
                "rotinaRevisao": ["Registrar os erros.", "Refazer os itens depois de dois dias."]
              },
              "avaliacaoERevisao": {
                "indicadoresProgresso": ["Precisão nas respostas.", "Clareza da justificativa."],
                "criteriosSucesso": ["Resolve problemas básicos.", "Explica o procedimento."],
                "prazoRevisao": "Ciclo sugerido de quatro semanas, ajustável pelo docente.",
                "pontosValidacaoDocente": ["Confirmar os conteúdos e o cronograma."]
              }
            }
            """;

    private ChatModel chatModel;
    private GeminiApiService service;

    @BeforeEach
    void configurar() {
        chatModel = mock(ChatModel.class);
        service = new GeminiApiService(chatModel, new ObjectMapper());
    }

    @Test
    void deveRenderizarTodasAsSecoesSemTabela() {
        ChatResponse resposta = criarResposta(JSON_VALIDO);
        when(chatModel.call(any(Prompt.class))).thenReturn(resposta);

        String html = gerarPlano();

        assertThat(html)
                .contains("<h2>1. Contexto e Condições Consideradas</h2>")
                .contains("<h2>2. Mapa de Conteúdos por Prioridade</h2>")
                .contains("<h2>5. Roteiro de Estudo por Semanas</h2>")
                .contains("<h2>7. Prática e Geração de Exercícios com IA</h2>")
                .contains("<h2>8. Avaliação Formativa, Revisão e Validação</h2>")
                .doesNotContain("<table", "<th", "<td");
    }

    @Test
    void deveTentarNovamenteQuandoPrimeiraRespostaForInvalida() {
        ChatResponse respostaInvalida = criarResposta("{json incompleto");
        ChatResponse respostaValida = criarResposta(JSON_VALIDO);
        when(chatModel.call(any(Prompt.class)))
                .thenReturn(respostaInvalida, respostaValida);
        MockMultipartFile anexo = new MockMultipartFile(
                "arquivo", "material.pdf", "application/pdf", "conteúdo".getBytes(StandardCharsets.UTF_8));

        String html = gerarPlano(anexo);

        assertThat(html).contains("Mapa de Conteúdos por Prioridade");
        ArgumentCaptor<Prompt> prompts = ArgumentCaptor.forClass(Prompt.class);
        verify(chatModel, times(2)).call(prompts.capture());
        assertThat(prompts.getAllValues().get(0).getUserMessage().getMedia()).hasSize(1);
        assertThat(prompts.getAllValues().get(1).getUserMessage().getMedia()).isEmpty();
        assertThat(prompts.getAllValues().get(1).getUserMessage().getText())
                .contains("resposta anterior", "{json incompleto");
    }

    @Test
    void deveRejeitarPlanoIncompletoDepoisDeDuasTentativas() {
        ChatResponse primeiraResposta = criarResposta("{}");
        ChatResponse segundaResposta = criarResposta("{}");
        when(chatModel.call(any(Prompt.class)))
                .thenReturn(primeiraResposta, segundaResposta);

        assertThatThrownBy(this::gerarPlano)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("após duas tentativas");
    }

    @Test
    void deveEscaparHtmlPresenteNosDadosDaIa() {
        String respostaComHtml = JSON_VALIDO.replace(
                "Plano para Cálculo I com foco em derivadas.",
                "Plano com <script>alert('x')</script> no texto.");
        ChatResponse resposta = criarResposta(respostaComHtml);
        when(chatModel.call(any(Prompt.class))).thenReturn(resposta);

        String html = gerarPlano();

        assertThat(html)
                .contains("&lt;script&gt;alert(&#39;x&#39;)&lt;/script&gt;")
                .doesNotContain("<script>");
    }

    @Test
    void naoDeveExibirMarcadoresInternosNoDocumento() {
        String respostaComMarcadores = JSON_VALIDO
                .replace(
                        "O cronograma precisa de validação docente.",
                        "O cronograma precisa de validação docente. (SUGESTAO_A_VALIDAR)")
                .replace(
                        "Confirmar os conteúdos e o cronograma.",
                        "Confirmar os conteúdos e o cronograma. (CONTEXTO)");
        ChatResponse resposta = criarResposta(respostaComMarcadores);
        when(chatModel.call(any(Prompt.class))).thenReturn(resposta);

        String html = gerarPlano();

        assertThat(html)
                .contains("O cronograma precisa de valida&ccedil;&atilde;o docente.")
                .doesNotContain("SUGESTAO_A_VALIDAR", "(CONTEXTO)", "<strong>Origem:</strong>");
    }

    private String gerarPlano() {
        return gerarPlano(null);
    }

    private String gerarPlano(MockMultipartFile arquivo) {
        return service.gerarPlanoDeApoioHtml(
                "Turma A", "Cálculo I", "1º semestre",
                "Aplicar derivadas", "Dificuldade de interpretação", "Monitoria", arquivo);
    }

    private ChatResponse criarResposta(String texto) {
        ChatResponse resposta = mock(ChatResponse.class);
        Generation geracao = mock(Generation.class);
        AssistantMessage mensagem = mock(AssistantMessage.class);
        when(resposta.getResult()).thenReturn(geracao);
        when(geracao.getOutput()).thenReturn(mensagem);
        when(mensagem.getText()).thenReturn(texto);
        return resposta;
    }
}
