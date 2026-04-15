package com.Seia.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "planos_apoio")
public class PlanosApoio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conteudo_gerado_ia", columnDefinition = "TEXT")
    private String conteudoGeradoIa;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dados")
    private DadosApoio dadosApoio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudoGeradoIa() {
        return conteudoGeradoIa;
    }

    public void setConteudoGeradoIa(String conteudoGeradoIa) {
        this.conteudoGeradoIa = conteudoGeradoIa;
    }

    public DadosApoio getDadosApoio() {
        return dadosApoio;
    }

    public void setDadosApoio(DadosApoio dadosApoio) {
        this.dadosApoio = dadosApoio;
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
        PlanosApoio that = (PlanosApoio) o;
        return Objects.equals(id, that.id) && Objects.equals(conteudoGeradoIa, that.conteudoGeradoIa) && Objects.equals(dadosApoio, that.dadosApoio) && Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, conteudoGeradoIa, dadosApoio, usuario);
    }
}
