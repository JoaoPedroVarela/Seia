package com.Seia.services;

import java.util.List;

final class ValidadorPlanoEstruturado {

    private ValidadorPlanoEstruturado() {
    }

    static void validar(PlanoEstruturado plano) {
        if (plano == null) throw new IllegalStateException("O plano estruturado está ausente.");

        validarTexto("síntese do contexto", plano.sinteseContexto());
        validarLista("premissas", plano.premissas(), 1);
        validarListaOpcional("informações ausentes", plano.informacoesAusentes());
        validarLista("mapa de conteúdos", plano.mapaConteudos(), 1);
        validarLista("revisões de pré-requisitos", plano.revisoesPrerequisitos(), 1);
        validarLista("objetivos de aprendizagem", plano.objetivosAprendizagem(), 3);
        validarLista("roteiro semanal", plano.roteiroSemanal(), 2);
        validarLista("apoios por barreira", plano.apoiosPorBarreira(), 1);

        plano.premissas().forEach(item -> validarTexto("premissa", item));
        plano.informacoesAusentes().forEach(item -> validarTexto("informação ausente", item));
        plano.mapaConteudos().forEach(ValidadorPlanoEstruturado::validarConteudo);
        plano.revisoesPrerequisitos().forEach(ValidadorPlanoEstruturado::validarRevisao);
        plano.objetivosAprendizagem().forEach(ValidadorPlanoEstruturado::validarObjetivo);
        plano.roteiroSemanal().forEach(ValidadorPlanoEstruturado::validarSemana);
        plano.apoiosPorBarreira().forEach(ValidadorPlanoEstruturado::validarApoio);
        validarPraticaComIa(plano.praticaComIa());
        validarAvaliacao(plano.avaliacaoERevisao());
    }

    private static void validarConteudo(ConteudoPrioritario conteudo) {
        if (conteudo == null || conteudo.prioridade() == null || conteudo.origem() == null) {
            throw new IllegalStateException("Um conteúdo priorizado está incompleto.");
        }
        validarTexto("tema prioritário", conteudo.tema());
        validarTexto("justificativa do conteúdo", conteudo.justificativa());
        validarListaOpcional("pré-requisitos do conteúdo", conteudo.prerequisitos());
        conteudo.prerequisitos().forEach(item -> validarTexto("pré-requisito do conteúdo", item));
        validarTexto("resultado esperado do conteúdo", conteudo.resultadoEsperado());
    }

    private static void validarRevisao(RevisaoPrerequisito revisao) {
        if (revisao == null) throw new IllegalStateException("Uma revisão de pré-requisito está incompleta.");
        validarTexto("conteúdo da revisão", revisao.conteudo());
        validarLista("atividades da revisão", revisao.atividades(), 1);
        revisao.atividades().forEach(item -> validarTexto("atividade da revisão", item));
        validarTexto("critério de domínio", revisao.criterioDominio());
    }

    private static void validarObjetivo(ObjetivoAprendizagem objetivo) {
        if (objetivo == null) throw new IllegalStateException("Um objetivo de aprendizagem está incompleto.");
        validarTexto("objetivo de aprendizagem", objetivo.descricao());
        validarTexto("nível de Bloom", objetivo.nivelBloom());
        validarTexto("evidência do objetivo", objetivo.evidencia());
    }

    private static void validarSemana(SemanaEstudo semana) {
        if (semana == null) throw new IllegalStateException("Uma etapa do roteiro semanal está incompleta.");
        validarTexto("período do roteiro", semana.periodo());
        validarTexto("objetivo semanal", semana.objetivo());
        validarLista("conteúdos semanais", semana.conteudos(), 1);
        validarLista("atividades de estudo", semana.atividadesEstudo(), 1);
        validarLista("exercícios", semana.exercicios(), 1);
        validarLista("apoios e adaptações", semana.apoiosAdaptacoes(), 1);
        validarLista("evidências de progresso", semana.evidenciasProgresso(), 1);
        semana.conteudos().forEach(item -> validarTexto("conteúdo semanal", item));
        semana.atividadesEstudo().forEach(item -> validarTexto("atividade de estudo", item));
        semana.exercicios().forEach(item -> validarTexto("exercício", item));
        semana.apoiosAdaptacoes().forEach(item -> validarTexto("apoio ou adaptação", item));
        semana.evidenciasProgresso().forEach(item -> validarTexto("evidência de progresso", item));
        validarTexto("critério de avanço", semana.criterioAvanco());
    }

    private static void validarApoio(ApoioBarreira apoio) {
        if (apoio == null) throw new IllegalStateException("Um apoio por barreira está incompleto.");
        validarTexto("barreira", apoio.barreira());
        validarTexto("impacto da barreira", apoio.impacto());
        validarLista("ações de apoio", apoio.acoes(), 1);
        validarLista("princípios pedagógicos", apoio.principiosPedagogicos(), 1);
        validarLista("recursos", apoio.recursos(), 1);
        validarLista("evidências da barreira", apoio.evidenciasProgresso(), 1);
        validarLista("acompanhamento", apoio.acompanhamento(), 1);
        apoio.acoes().forEach(item -> validarTexto("ação de apoio", item));
        apoio.principiosPedagogicos().forEach(item -> validarTexto("princípio pedagógico", item));
        apoio.recursos().forEach(item -> validarTexto("recurso", item));
        apoio.evidenciasProgresso().forEach(item -> validarTexto("evidência da barreira", item));
        apoio.acompanhamento().forEach(item -> validarTexto("forma de acompanhamento", item));
    }

    private static void validarPraticaComIa(PraticaComIa pratica) {
        if (pratica == null) throw new IllegalStateException("A seção de prática com IA está ausente.");
        validarLista("orientações de uso da IA", pratica.orientacoesUso(), 2);
        validarTexto("prompt de exercícios", pratica.promptExercicios());
        validarLista("rotina de revisão", pratica.rotinaRevisao(), 2);
        pratica.orientacoesUso().forEach(item -> validarTexto("orientação de uso da IA", item));
        pratica.rotinaRevisao().forEach(item -> validarTexto("etapa da rotina de revisão", item));
    }

    private static void validarAvaliacao(AvaliacaoERevisao avaliacao) {
        if (avaliacao == null) throw new IllegalStateException("A seção de avaliação e revisão está ausente.");
        validarLista("indicadores de progresso", avaliacao.indicadoresProgresso(), 2);
        validarLista("critérios de sucesso", avaliacao.criteriosSucesso(), 2);
        validarTexto("prazo de revisão", avaliacao.prazoRevisao());
        validarLista("pontos de validação docente", avaliacao.pontosValidacaoDocente(), 1);
        avaliacao.indicadoresProgresso().forEach(item -> validarTexto("indicador de progresso", item));
        avaliacao.criteriosSucesso().forEach(item -> validarTexto("critério de sucesso", item));
        avaliacao.pontosValidacaoDocente().forEach(item -> validarTexto("ponto de validação docente", item));
    }

    private static void validarTexto(String campo, String valor) {
        if (valor == null || valor.isBlank() || valor.length() > 4_000) {
            throw new IllegalStateException("O campo '" + campo + "' está ausente ou inválido.");
        }
    }

    private static void validarLista(String campo, List<?> itens, int minimo) {
        if (itens == null || itens.size() < minimo) {
            throw new IllegalStateException(
                    "A seção '" + campo + "' deve conter pelo menos " + minimo + " item(ns).");
        }
    }

    private static void validarListaOpcional(String campo, List<?> itens) {
        if (itens == null) {
            throw new IllegalStateException("A seção opcional '" + campo + "' deve ser informada como lista.");
        }
    }
}
