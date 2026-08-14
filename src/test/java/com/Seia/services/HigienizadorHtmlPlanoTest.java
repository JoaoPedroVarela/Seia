package com.Seia.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HigienizadorHtmlPlanoTest {

    private final HigienizadorHtmlPlano higienizador = new HigienizadorHtmlPlano();

    @Test
    void devePreservarSomenteCoresValidasDoEditor() {
        String html = """
                <h2>Título</h2>
                <p><span style="color: rgb(230, 0, 0); background-color: #ffff00; font-size: 40px">Texto</span></p>
                """;

        String resultado = higienizador.higienizar(html);

        assertThat(resultado)
                .contains("color: rgb(230, 0, 0)")
                .contains("background-color: #ffff00")
                .doesNotContain("font-size");
    }

    @Test
    void deveRemoverScriptsEventosLinksEEstilosPerigosos() {
        String html = """
                <script>alert('x')</script>
                <p onclick="alert('x')">Texto</p>
                <img src="x" onerror="alert('x')">
                <span style="color: expression(alert('x')); background-image: url(javascript:alert('x'))">Cor</span>
                <a href="javascript:alert('x')">link</a>
                """;

        String resultado = higienizador.higienizar(html);

        assertThat(resultado)
                .contains("<p>Texto</p>", "Cor", "link")
                .doesNotContain("script", "onclick", "onerror", "javascript", "expression", "<img", "<a");
    }

    @Test
    void deveRejeitarComponentesRgbForaDoIntervalo() {
        String resultado = higienizador.higienizar(
                "<span style=\"color: rgb(999, 0, 0); background-color: rgba(0, 0, 0, 2)\">Texto</span>"
        );

        assertThat(resultado).isEqualTo("<span>Texto</span>");
    }
}
