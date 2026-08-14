package com.Seia.services;

import java.util.List;

record PlanoEstruturado(String sinteseContexto, List<String> premissas,
                        List<String> informacoesAusentes, List<ConteudoPrioritario> mapaConteudos,
                        List<RevisaoPrerequisito> revisoesPrerequisitos,
                        List<ObjetivoAprendizagem> objetivosAprendizagem,
                        List<SemanaEstudo> roteiroSemanal, List<ApoioBarreira> apoiosPorBarreira,
                        PraticaComIa praticaComIa, AvaliacaoERevisao avaliacaoERevisao) {
}

record ConteudoPrioritario(String tema, PrioridadeConteudo prioridade, OrigemConteudo origem,
                           String justificativa, List<String> prerequisitos, String resultadoEsperado) {
}

record RevisaoPrerequisito(String conteudo, List<String> atividades, String criterioDominio) {
}

record ObjetivoAprendizagem(String descricao, String nivelBloom, String evidencia) {
}

record SemanaEstudo(String periodo, String objetivo, List<String> conteudos,
                    List<String> atividadesEstudo, List<String> exercicios,
                    List<String> apoiosAdaptacoes, List<String> evidenciasProgresso,
                    String criterioAvanco) {
}

record ApoioBarreira(String barreira, String impacto, List<String> acoes,
                     List<String> principiosPedagogicos, List<String> recursos,
                     List<String> evidenciasProgresso, List<String> acompanhamento) {
}

record PraticaComIa(List<String> orientacoesUso, String promptExercicios,
                    List<String> rotinaRevisao) {
}

record AvaliacaoERevisao(List<String> indicadoresProgresso, List<String> criteriosSucesso,
                         String prazoRevisao, List<String> pontosValidacaoDocente) {
}

enum PrioridadeConteudo { ESSENCIAL, IMPORTANTE, APROFUNDAMENTO }

enum OrigemConteudo { CONTEXTO, ANEXO, SUGESTAO_A_VALIDAR }
