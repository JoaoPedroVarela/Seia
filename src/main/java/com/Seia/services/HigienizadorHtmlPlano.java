package com.Seia.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HigienizadorHtmlPlano {

    private static final Set<String> PROPRIEDADES_DE_COR = Set.of("color", "background-color");
    private static final Pattern COR_HEXADECIMAL =
            Pattern.compile("^#(?:[0-9a-fA-F]{3}|[0-9a-fA-F]{4}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$");
    private static final Pattern COR_RGB = Pattern.compile(
            "^rgba?\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})(?:\\s*,\\s*(0|1|0?\\.\\d+))?\\s*\\)$",
            Pattern.CASE_INSENSITIVE
    );
    private static final Safelist ELEMENTOS_PERMITIDOS = Safelist.none()
            .addTags("h2", "h3", "p", "ul", "ol", "li", "strong", "em", "u", "span", "br")
            .addAttributes("span", "style");

    public String higienizar(String html) {
        Document documento = Jsoup.parseBodyFragment(html == null ? "" : html);
        documento.select("[style]").forEach(this::higienizarEstilos);

        Document.OutputSettings configuracao = new Document.OutputSettings()
                .charset(StandardCharsets.UTF_8)
                .prettyPrint(false);
        return Jsoup.clean(documento.body().html(), "", ELEMENTOS_PERMITIDOS, configuracao).trim();
    }

    private void higienizarEstilos(Element elemento) {
        List<String> estilosPermitidos = new ArrayList<>();

        for (String declaracao : elemento.attr("style").split(";")) {
            String[] partes = declaracao.split(":", 2);
            if (partes.length != 2) continue;

            String propriedade = partes[0].trim().toLowerCase(Locale.ROOT);
            String valor = partes[1].trim();
            if (PROPRIEDADES_DE_COR.contains(propriedade) && corValida(valor)) {
                estilosPermitidos.add(propriedade + ": " + valor);
            }
        }

        if (estilosPermitidos.isEmpty()) {
            elemento.removeAttr("style");
        } else {
            elemento.attr("style", String.join("; ", estilosPermitidos));
        }
    }

    private boolean corValida(String valor) {
        if (COR_HEXADECIMAL.matcher(valor).matches()) return true;

        Matcher rgb = COR_RGB.matcher(valor);
        if (!rgb.matches()) return false;

        for (int grupo = 1; grupo <= 3; grupo++) {
            if (Integer.parseInt(rgb.group(grupo)) > 255) return false;
        }
        return rgb.group(4) == null || Double.parseDouble(rgb.group(4)) <= 1;
    }
}
