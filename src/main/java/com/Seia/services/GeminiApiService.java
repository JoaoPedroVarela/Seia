package com.Seia.services;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class GeminiApiService {

    private final ChatModel chatModel;

    public GeminiApiService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String gerarPlanoDeApoioHtml(String nome, String materia, String serie,
                                        String conteudos, String barreiras, String solucaoNecessaria) {

        String prompt = """
                Você é um especialista em educação e psicopedagogia.
                Crie um rascunho de Plano de Apoio para o contexto abaixo.
                
                DADOS:
                - Aluno/Turma: %s
                - Matéria: %s
                - Série: %s
                - Conteúdos trabalhados: %s
                - Barreiras percebidas: %s
                - Soluções/Apoios sugeridos pelo professor: %s
                
                DIRETRIZES DO PLANO:
                O plano deve conter seções para: Metas Claras, Estratégias e Adaptações Práticas, e Maneiras de Avaliar, incluindo justificativas pedagógicas breves.
                
                REGRAS ESTRITAS DE FORMATAÇÃO:
                1. Retorne APENAS conteúdo HTML estruturado para ser embutido em uma página (use tags como <h2>, <h3>, <p>, <ul>, <li>, <strong>).
                2. NÃO inclua as tags base do HTML como <html>, <head>, <title> ou <body>.
                3. NÃO utilize formatação Markdown (NÃO coloque ```html no começo nem ``` no final). Entregue o texto HTML puro.
                """.formatted(nome, materia, serie, conteudos, barreiras, solucaoNecessaria);

        return chatModel.call(prompt);
    }
}