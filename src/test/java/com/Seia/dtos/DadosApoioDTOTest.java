package com.Seia.dtos;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DadosApoioDTOTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void configurarValidacao() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void encerrarValidacao() {
        validatorFactory.close();
    }

    @Test
    void deveAceitarContextoAcademicoCompleto() {
        var dto = new DadosApoioDTO(
                "Engenharia de Software - noturno",
                "Cálculo I",
                "1º semestre",
                "Aplicar derivadas em problemas de otimização.",
                "Foram observadas lacunas na interpretação dos enunciados.",
                "Monitoria semanal e avaliação formativa."
        );

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void deveRejeitarCamposVaziosEContextoInsuficiente() {
        var dto = new DadosApoioDTO(" ", "TI", " ", "curto", "pouco", null);

        var camposInvalidos = validator.validate(dto).stream()
                .map(violacao -> violacao.getPropertyPath().toString())
                .collect(java.util.stream.Collectors.toSet());

        assertThat(camposInvalidos)
                .contains("nome", "materia", "serie", "conteudos", "barreiras");
    }
}
