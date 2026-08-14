package com.Seia.services;

import org.springframework.web.util.HtmlUtils;

import java.util.List;

final class RenderizadorHtmlPlano {

    private RenderizadorHtmlPlano() {
    }

    static String renderizar(PlanoEstruturado plano) {
        StringBuilder html = new StringBuilder(16_000);

        html.append("<h2>1. Contexto e Condições Consideradas</h2>");
        appendParagrafo(html, plano.sinteseContexto());
        appendListaComRotulo(html, "Condições consideradas", plano.premissas());
        if (plano.informacoesAusentes().isEmpty()) {
            appendParagrafoComRotulo(html, "Informações ausentes", "Nenhuma informação crítica adicional foi identificada.");
        } else {
            appendListaComRotulo(html, "Informações que precisam de confirmação", plano.informacoesAusentes());
        }

        html.append("<h2>2. Mapa de Conteúdos por Prioridade</h2>");
        for (ConteudoPrioritario conteudo : plano.mapaConteudos()) {
            html.append("<h3>")
                    .append(escapar(rotuloPrioridade(conteudo.prioridade())))
                    .append(" — ")
                    .append(escapar(conteudo.tema()))
                    .append("</h3>");
            appendParagrafoComRotulo(html, "Por que estudar", conteudo.justificativa());
            if (!conteudo.prerequisitos().isEmpty()) appendListaComRotulo(html, "Pré-requisitos", conteudo.prerequisitos());
            appendParagrafoComRotulo(html, "Resultado esperado", conteudo.resultadoEsperado());
        }

        html.append("<h2>3. Revisões de Pré-requisitos</h2>");
        for (RevisaoPrerequisito revisao : plano.revisoesPrerequisitos()) {
            appendTituloTerceiroNivel(html, revisao.conteudo());
            appendListaComRotulo(html, "Como revisar", revisao.atividades());
            appendParagrafoComRotulo(html, "Critério de domínio", revisao.criterioDominio());
        }

        html.append("<h2>4. Objetivos de Aprendizagem Observáveis</h2><ol>");
        for (ObjetivoAprendizagem objetivo : plano.objetivosAprendizagem()) {
            html.append("<li><strong>").append(escapar(objetivo.descricao()))
                    .append("</strong><ul><li><strong>Nível de Bloom:</strong> ")
                    .append(escapar(objetivo.nivelBloom()))
                    .append("</li><li><strong>Evidência esperada:</strong> ")
                    .append(escapar(objetivo.evidencia())).append("</li></ul></li>");
        }
        html.append("</ol>");

        html.append("<h2>5. Roteiro de Estudo por Semanas</h2>");
        for (SemanaEstudo semana : plano.roteiroSemanal()) {
            appendTituloTerceiroNivel(html, semana.periodo());
            appendParagrafoComRotulo(html, "Objetivo", semana.objetivo());
            appendListaComRotulo(html, "Conteúdos", semana.conteudos());
            appendListaComRotulo(html, "Atividades de estudo", semana.atividadesEstudo());
            appendListaComRotulo(html, "Prática e exercícios", semana.exercicios());
            appendListaComRotulo(html, "Apoios e adaptações", semana.apoiosAdaptacoes());
            appendListaComRotulo(html, "Evidências de progresso", semana.evidenciasProgresso());
            appendParagrafoComRotulo(html, "Critério para avançar", semana.criterioAvanco());
        }

        html.append("<h2>6. Estratégias de Apoio por Barreira</h2>");
        for (ApoioBarreira apoio : plano.apoiosPorBarreira()) {
            appendTituloTerceiroNivel(html, apoio.barreira());
            appendParagrafoComRotulo(html, "Impacto observado", apoio.impacto());
            appendListaComRotulo(html, "Ações sugeridas", apoio.acoes());
            appendListaComRotulo(html, "Princípios pedagógicos", apoio.principiosPedagogicos());
            appendListaComRotulo(html, "Recursos", apoio.recursos());
            appendListaComRotulo(html, "Evidências de progresso", apoio.evidenciasProgresso());
            appendListaComRotulo(html, "Acompanhamento", apoio.acompanhamento());
        }

        html.append("<h2>7. Prática e Geração de Exercícios com IA</h2>");
        appendListaComRotulo(html, "Orientações de uso", plano.praticaComIa().orientacoesUso());
        appendParagrafoComRotulo(html, "Prompt reutilizável", plano.praticaComIa().promptExercicios());
        appendListaComRotulo(html, "Rotina de revisão", plano.praticaComIa().rotinaRevisao());

        html.append("<h2>8. Avaliação Formativa, Revisão e Validação</h2>");
        appendListaComRotulo(html, "Indicadores de progresso", plano.avaliacaoERevisao().indicadoresProgresso());
        appendListaComRotulo(html, "Critérios de sucesso", plano.avaliacaoERevisao().criteriosSucesso());
        appendParagrafoComRotulo(html, "Prazo de revisão", plano.avaliacaoERevisao().prazoRevisao());
        appendListaComRotulo(html, "Pontos para validação docente/institucional", plano.avaliacaoERevisao().pontosValidacaoDocente());

        return html.toString();
    }

    private static void appendTituloTerceiroNivel(StringBuilder html, String titulo) {
        html.append("<h3>").append(escapar(titulo)).append("</h3>");
    }

    private static void appendParagrafo(StringBuilder html, String texto) {
        html.append("<p>").append(escapar(texto)).append("</p>");
    }

    private static void appendParagrafoComRotulo(StringBuilder html, String rotulo, String texto) {
        html.append("<p><strong>").append(escapar(rotulo)).append(":</strong> ")
                .append(escapar(texto)).append("</p>");
    }

    private static void appendListaComRotulo(StringBuilder html, String rotulo, List<String> itens) {
        html.append("<p><strong>").append(escapar(rotulo)).append(":</strong></p><ul>");
        for (String item : itens) html.append("<li>").append(escapar(item)).append("</li>");
        html.append("</ul>");
    }

    private static String escapar(String valor) {
        String semMarcadorInterno = valor.replaceFirst(
                "(?i)\\s*\\((CONTEXTO|ANEXO|SUGESTAO_A_VALIDAR)\\)\\s*$",
                ""
        );
        return HtmlUtils.htmlEscape(semMarcadorInterno);
    }

    private static String rotuloPrioridade(PrioridadeConteudo prioridade) {
        return switch (prioridade) {
            case ESSENCIAL -> "Prioridade essencial";
            case IMPORTANTE -> "Conteúdo importante";
            case APROFUNDAMENTO -> "Aprofundamento";
        };
    }
}
