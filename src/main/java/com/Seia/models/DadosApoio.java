package com.Seia.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "dados_apoio")
public class DadosApoio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String materia;

    @Column(nullable = false, length = 100)
    private String serie;

    @Column(nullable = false)
    private String conteudos;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String barreiras;

    @Column(name = "solucao_necessaria", nullable = false, columnDefinition = "TEXT")
    private String solucaoNecessaria;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getConteudos() {
        return conteudos;
    }

    public void setConteudos(String conteudos) {
        this.conteudos = conteudos;
    }

    public String getBarreiras() {
        return barreiras;
    }

    public void setBarreiras(String barreiras) {
        this.barreiras = barreiras;
    }

    public String getSolucaoNecessaria() {
        return solucaoNecessaria;
    }

    public void setSolucaoNecessaria(String solucaoNecessaria) {
        this.solucaoNecessaria = solucaoNecessaria;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DadosApoio that = (DadosApoio) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && Objects.equals(materia, that.materia) && Objects.equals(serie, that.serie) && Objects.equals(conteudos, that.conteudos) && Objects.equals(barreiras, that.barreiras) && Objects.equals(solucaoNecessaria, that.solucaoNecessaria) && Objects.equals(dataCriacao, that.dataCriacao) && Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, materia, serie, conteudos, barreiras, solucaoNecessaria, dataCriacao, usuario);
    }
}
